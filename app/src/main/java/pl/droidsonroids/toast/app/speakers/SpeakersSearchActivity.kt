package pl.droidsonroids.toast.app.speakers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_speakers_search.*
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.utils.LazyLoadingScrollListener
import pl.droidsonroids.toast.databinding.ActivitySpeakersSearchBinding
import pl.droidsonroids.toast.viewmodels.speaker.SpeakersSearchViewModel

class SpeakersSearchActivity : BaseActivity() {

    companion object {
        val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
        fun createIntent(context: Context): Intent = Intent(context, SpeakersSearchActivity::class.java)
    }

    private val speakersSearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SpeakersSearchViewModel::class.java]
    }

    private var speakersDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val speakersSearchBinding = ActivitySpeakersSearchBinding.inflate(layoutInflater)
        setContentView(speakersSearchBinding.root)
        setupToolbar()
        setupViewModel(speakersSearchBinding)
        setupRecyclerView()
        setupSearchBox()

        val haveNoSavedInstances = savedInstanceState == null
        showEnterAnimation(isAnimationNeeded = haveNoSavedInstances && hasCircularRevealExtras())
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> consume { showParentActivityWithAnimation() }
                else -> super.onOptionsItemSelected(item)
            }

    private fun setupSearchBox() {
        searchBox.requestFocus()
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

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel(speakersSearchBinding: ActivitySpeakersSearchBinding) {
        speakersSearchBinding.speakersSearchViewModel = speakersSearchViewModel

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

    private fun showEnterAnimation(isAnimationNeeded: Boolean) {
        if (isAnimationNeeded) {
            val revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
            val revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)

            with(toolbar.viewTreeObserver) {
                if (isAlive) {
                    addOnGlobalLayoutListener(this, revealX, revealY)
                }
            }
        }
    }

    private fun addOnGlobalLayoutListener(viewTreeObserver: ViewTreeObserver, revealX: Int, revealY: Int) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                showRevealAnimation(revealX, revealY)
                toolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun hasCircularRevealExtras() =
            intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)

    private fun showRevealAnimation(rootX: Int, rootY: Int) {
        val finalRadius = getToolbarRadius()
        val revealAnimation = ViewAnimationUtils.createCircularReveal(toolbar, rootX, rootY, 0f, finalRadius)

        with(revealAnimation) {
            duration = 300
            interpolator = AccelerateInterpolator()
            start()
        }
    }

    private fun getToolbarRadius() = (Math.max(toolbar.width, toolbar.height)).toFloat()

    private fun showParentActivityWithAnimation() {
        if (hasCircularRevealExtras()) {
            val revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X,0)
            val revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)
            val finalRadius = getToolbarRadius()
            val circularReveal = ViewAnimationUtils.createCircularReveal(toolbar, revealX, revealY, finalRadius, 0f)

            with(circularReveal) {
                duration = 300
                addAnimationEndListener(this)
                start()
            }
        } else {
            NavUtils.navigateUpFromSameTask(this@SpeakersSearchActivity)
        }
    }

    private fun addAnimationEndListener(animator: Animator) {
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                toolbar.visibility = View.INVISIBLE
                NavUtils.navigateUpFromSameTask(this@SpeakersSearchActivity)
                overridePendingTransition(0, 0)
            }
        })
    }

    private fun consume(func: () -> Unit): Boolean {
        func()
        return true
    }

    override fun onDestroy() {
        speakersDisposable?.dispose()
        super.onDestroy()
    }
}