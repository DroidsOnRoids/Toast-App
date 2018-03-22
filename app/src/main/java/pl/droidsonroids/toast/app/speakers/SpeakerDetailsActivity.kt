package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionSet
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_speaker_details.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.events.HorizontalSnapHelper
import pl.droidsonroids.toast.app.utils.SnackbarQueue
import pl.droidsonroids.toast.app.utils.binding.setVisible
import pl.droidsonroids.toast.app.utils.extensions.addInsetAppBehaviorToLoadingLayout
import pl.droidsonroids.toast.app.utils.extensions.doOnEnd
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.databinding.ActivitySpeakerDetailsBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerDetailsViewModel
import javax.inject.Inject

class SpeakerDetailsActivity : BaseActivity() {
    companion object {
        private const val SPEAKER_ID: String = "speaker_id"
        private const val AVATAR_IMAGE = "avatar_image"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SpeakerDetails): Intent {
            return Intent(context, SpeakerDetailsActivity::class.java)
                    .putExtra(SPEAKER_ID, navigationRequest.id)
                    .putExtra(AVATAR_IMAGE, navigationRequest.avatar)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var snackbarQueue: SnackbarQueue

    private val speakerDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(speakerId.toString(), SpeakerDetailsViewModel::class.java)
    }
    private val speakerId: Long by lazy {
        intent.getLongExtra(SPEAKER_ID, Constants.NO_ID)
    }

    private val avatar by lazy {
        intent.getParcelableExtra<ImageDto?>(AVATAR_IMAGE)
    }

    private val compositeDisposable = CompositeDisposable()
    private var isTransitionPostponed = false

    private var isAvatarAnimationShowing = false

    override fun showSnackbar(request: NavigationRequest.SnackBar): Boolean {
        snackbarQueue.postSnackbarRequest(request)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val speakerDetailsBinding = ActivitySpeakerDetailsBinding.inflate(layoutInflater)
        setContentView(speakerDetailsBinding.root)
        setupToolbar()
        setupViewModel(speakerDetailsBinding)
        postponeSharedTransitionIfNeeded(isFreshStart = savedInstanceState == null)
        setupRecyclerView()
        addInsetAppBehaviorToLoadingLayout()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel(speakerDetailsBinding: ActivitySpeakerDetailsBinding) {
        speakerDetailsViewModel.init(speakerId, avatar)
        speakerDetailsBinding.speakerDetailsViewModel = speakerDetailsViewModel
        compositeDisposable += speakerDetailsViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
        compositeDisposable += speakerDetailsViewModel.avatarLoadingFinishedSubject
                .filter { isTransitionPostponed }
                .subscribe { resumeSharedTransition() }
    }


    private fun postponeSharedTransitionIfNeeded(isFreshStart: Boolean) {
        if (isFreshStart) {
            postponeEnterTransition()
            window.sharedElementEnterTransition = TransitionSet()
                    .addTransition(ChangeImageTransform())
                    .addTransition(ChangeBounds())
                    .doOnEnd { speakerDetailsViewModel.onTransitionEnd() }
            isTransitionPostponed = true
        } else {
            speakerDetailsViewModel.onTransitionEnd()
        }
    }

    private fun resumeSharedTransition() {
        startPostponedEnterTransition()
        isTransitionPostponed = false
    }

    private fun handleNavigationRequest(request: NavigationRequest) {
        when (request) {
            is NavigationRequest.SpeakerTalkDetails -> showTalkDetails(request)
            is NavigationRequest.EventDetails -> showEventDetails(request)
            NavigationRequest.AvatarAnimation -> toggleAvatarAnimation()
            else -> navigator.dispatch(this, request)
        }
    }

    private fun toggleAvatarAnimation() {
        if (isAvatarAnimationShowing) {
            stopAvatarAnimation()
        } else {
            showAvatarAnimation()
        }
    }

    private fun showAvatarAnimation() {
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_rotation)
        avatarImage.startAnimation(rotateAnimation)
        isAvatarAnimationShowing = true
    }

    private fun stopAvatarAnimation() {
        avatarImage.clearAnimation()
        isAvatarAnimationShowing = false
    }

    private fun showTalkDetails(request: NavigationRequest.SpeakerTalkDetails) {
        navigator.showActivityWithSharedAnimation(this, request, getTalkSharedViews(request.speakerTalkDto))
    }

    private fun showEventDetails(request: NavigationRequest.EventDetails) {
        navigator.showActivityWithSharedAnimation(this, request, getEventSharedViews(request.talkId))
    }

    private fun getTalkSharedViews(speakerTalkDto: SpeakerTalkDto): Array<Pair<View, String>> {
        return talksRecyclerView.findViewHolderForItemId(speakerTalkDto.id)
                ?.itemView
                ?.run {
                    val talkCard = findViewById<View>(R.id.talkCard)
                    arrayOf(Pair(talkCard, talkCard.transitionName))
                } ?: emptyArray()
    }

    private fun getEventSharedViews(talkId: Long?): Array<Pair<View, String>> {
        return talkId?.let {
            talksRecyclerView.findViewHolderForItemId(it)
                    ?.itemView
                    ?.run {
                        val eventCoverImage = findViewById<View>(R.id.eventCoverImage)
                        arrayOf(Pair(eventCoverImage, eventCoverImage.transitionName))
                    }
        } ?: emptyArray()
    }

    private fun setupRecyclerView() {
        with(talksRecyclerView) {
            val talksAdapter = SpeakerTalksAdapter()
            adapter = talksAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            HorizontalSnapHelper(layoutManager, snapToLast = true).attachToRecyclerView(this)
            subscribeToTalksChanges(talksAdapter)
        }
    }

    private fun subscribeToTalksChanges(talksAdapter: SpeakerTalksAdapter) {
        compositeDisposable += speakerDetailsViewModel.talksSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { talksAdapter.setData(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> consume { onBackPressed() }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        snackbarQueue.attach(speakerDetailsContainer)
    }

    override fun onPause() {
        super.onPause()
        snackbarQueue.detach()
    }

    override fun onBackPressed() {
        avatarBorderContainer.setVisible(false)
        stopAvatarAnimation()
        super.onBackPressed()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}