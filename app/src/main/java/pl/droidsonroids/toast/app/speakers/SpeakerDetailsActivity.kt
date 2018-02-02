package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_speaker_details.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_speaker_details.*
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.events.HorizontalSnapHelper
import pl.droidsonroids.toast.databinding.ActivitySpeakerDetailsBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerDetailsViewModel
import javax.inject.Inject
import kotlin.math.abs

class SpeakerDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        private const val SPEAKER_ID: String = "speaker_id"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SpeakerDetails): Intent {
            return Intent(context, SpeakerDetailsActivity::class.java)
                    .putExtra(SPEAKER_ID, navigationRequest.id)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val speakerDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(speakerId.toString(), SpeakerDetailsViewModel::class.java)
    }

    private val speakerId: Long by lazy {
        intent.getLongExtra(SPEAKER_ID, Constants.NO_ID)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val speakerDetailsBinding = ActivitySpeakerDetailsBinding.inflate(layoutInflater)
        setContentView(speakerDetailsBinding.root)
        setupViewModel(speakerDetailsBinding)
        setupToolbar()
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> consume { finish() }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        setToolbarContentVisibility(verticalOffset)
    }

    private fun setToolbarContentVisibility(verticalOffset: Int) {
        if (abs(verticalOffset) == abs(appBar.height - toolbar.height)) {
            toolbarAvatarImage.visibility = View.VISIBLE
            toolbarSpeakerName.visibility = View.VISIBLE
        } else {
            toolbarAvatarImage.visibility = View.INVISIBLE
            toolbarSpeakerName.visibility = View.INVISIBLE
        }
    }

    private fun setupViewModel(speakerDetailsBinding: ActivitySpeakerDetailsBinding) {
        speakerDetailsViewModel.init(speakerId)
        speakerDetailsBinding.speakerDetailsViewModel = speakerDetailsViewModel
        compositeDisposable += speakerDetailsViewModel.navigationSubject
                .subscribe { navigator.dispatch(activity = this, navigationRequest = it) }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appBar.addOnOffsetChangedListener(this)
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

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}