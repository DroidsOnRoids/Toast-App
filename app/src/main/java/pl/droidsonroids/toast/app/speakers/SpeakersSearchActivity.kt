package pl.droidsonroids.toast.app.speakers

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_speakers_search.*
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.utils.*
import pl.droidsonroids.toast.databinding.ActivitySpeakersSearchBinding
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.speaker.SpeakersSearchViewModel
import javax.inject.Inject


class SpeakersSearchActivity : BaseActivity() {

    companion object {
        private const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        private const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"

        fun createIntent(context: Context, revealCenterX: Int, revealCenterY: Int): Intent =
                Intent(context, SpeakersSearchActivity::class.java)
                        .putExtra(EXTRA_CIRCULAR_REVEAL_X, revealCenterX)
                        .putExtra(EXTRA_CIRCULAR_REVEAL_Y, revealCenterY)
    }

    @Inject
    lateinit var navigator: Navigator

    private val speakersSearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SpeakersSearchViewModel::class.java]
    }

    private var speakersDisposable: Disposable = Disposables.disposed()

    private var navigationDisposable: Disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val speakersSearchBinding = ActivitySpeakersSearchBinding.inflate(layoutInflater)
        setContentView(speakersSearchBinding.root)
        setupViewModel(speakersSearchBinding)
        setupToolbar()
        setupRecyclerView()
        setupSearchBox()

        val haveNoSavedInstances = savedInstanceState == null
        setupEnterAnimation(isAnimationNeeded = haveNoSavedInstances && hasCircularRevealExtras())
    }

    override fun onBackPressed() {
        showParentWithLeavingAnimation()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> consume { showParentWithLeavingAnimation() }
                else -> super.onOptionsItemSelected(item)
            }

    private fun setupSearchBox() {
        searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                speakersSearchViewModel.requestSearch()
                searchBox.clearFocus()
                hideKeyboard()
            }
            true
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchBox.windowToken, 0)
    }

    private fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel(speakersSearchBinding: ActivitySpeakersSearchBinding) {
        speakersSearchBinding.speakersSearchViewModel = speakersSearchViewModel
        navigationDisposable = speakersSearchViewModel.navigationSubject
                .subscribe { navigator.dispatch(this, it) }
    }

    private fun setupRecyclerView() {
        with(speakersSearchRecyclerView) {
            val speakersAdapter = SpeakersAdapter()
            adapter = speakersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpeakerItemDecoration(context.applicationContext))
            addOnScrollListener(LazyLoadingScrollListener {
                speakersSearchViewModel.loadNextPage()
            })

            subscribeToSpeakersChange(speakersAdapter)
        }
    }

    private fun subscribeToSpeakersChange(speakersAdapter: SpeakersAdapter) {
        speakersDisposable = speakersSearchViewModel.speakersSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speakersAdapter::setData)
    }

    private fun showParentWithLeavingAnimation() {
        hideKeyboard()
        val animatorSet = AnimatorSet()
        val (centerX, centerY) = getAnimationCenterCoordinates()
        val toolbarAnimator = getToolbarAnimator(centerX, centerY, false) {
            toolbar.visibility = View.INVISIBLE
        }
        val contentAnimator = getContentAnimator(fromAlpha = 1f, toAlpha = 0f)
        animatorSet.playTogether(toolbarAnimator, contentAnimator)
        animatorSet.addListener(object : AnimatorEndListener {
            override fun onAnimationEnd(animator: Animator) {
                NavUtils.navigateUpFromSameTask(this@SpeakersSearchActivity)
                disableActivityTransitionAnimations()
            }
        })
        animatorSet.start()
    }

    private fun getContentAnimator(fromAlpha: Float, toAlpha: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(speakersSearchContainer, "alpha", fromAlpha, toAlpha).apply {
            interpolator = AccelerateInterpolator()
            duration = 300
        }
    }

    private fun getToolbarAnimator(centerX: Int, centerY: Int, isGrowing: Boolean, endAction: (() -> Unit)? = null): Animator {
        return RevealAnimatorBuilder.build(toolbar, centerX, centerY, isGrowing).apply {
            addListener(object : AnimatorEndListener {
                override fun onAnimationEnd(animator: Animator) {
                    endAction?.invoke()
                }
            })
        }
    }

    private fun setupEnterAnimation(isAnimationNeeded: Boolean) {
        if (isAnimationNeeded) {
            ViewTreeObserverBuilder.build(toolbar) {
                showEnterAnimation()
            }
        }
    }

    private fun showEnterAnimation() {
        val animatorSet = AnimatorSet()
        val (centerX, centerY) = getAnimationCenterCoordinates()

        val toolbarAnimator = getToolbarAnimator(centerX, centerY, true) {
            searchBox.requestFocus()
            showKeyboard()
        }

        val contentAnimator = getContentAnimator(fromAlpha = 0f, toAlpha = 1f)
        animatorSet.playTogether(toolbarAnimator, contentAnimator)
        animatorSet.start()
    }

    private fun getAnimationCenterCoordinates(): Pair<Int, Int> {
        val centerX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
        val centerY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)
        return Pair(centerX, centerY)
    }

    private fun hasCircularRevealExtras() =
            intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)

    override fun onDestroy() {
        speakersDisposable.dispose()
        navigationDisposable.dispose()
        super.onDestroy()
    }
}