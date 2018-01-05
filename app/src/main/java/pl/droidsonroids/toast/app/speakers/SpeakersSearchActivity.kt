package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_speakers_search.*
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.app.utils.LazyLoadingScrollListener
import pl.droidsonroids.toast.app.utils.RevealAnimationCreator
import pl.droidsonroids.toast.databinding.ActivitySpeakersSearchBinding
import pl.droidsonroids.toast.viewmodels.speaker.SpeakersSearchViewModel
import javax.inject.Inject


class SpeakersSearchActivity : BaseActivity() {

    companion object {
        val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"

        fun createIntent(context: Context, revealCenterX: Int, revealCenterY: Int): Intent {
            val intent = Intent(context, SpeakersSearchActivity::class.java)
            intent.putExtra(EXTRA_CIRCULAR_REVEAL_X, revealCenterX)
            intent.putExtra(EXTRA_CIRCULAR_REVEAL_Y, revealCenterY)
            return intent
        }
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
        showEnterAnimation(isAnimationNeeded = haveNoSavedInstances && haveCircularRevealExtras())
    }

    override fun onBackPressed() {
        showParentWithoutAnimation()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> consume { showParentWithoutAnimation() }
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

    private fun showEnterAnimation(isAnimationNeeded: Boolean) {
        if (isAnimationNeeded) {
            val animationCenterX = intent.getIntExtra(SpeakersSearchActivity.EXTRA_CIRCULAR_REVEAL_X, 0)
            val animationCenterY = intent.getIntExtra(SpeakersSearchActivity.EXTRA_CIRCULAR_REVEAL_Y, 0)
            RevealAnimationCreator.showAnimation(toolbar, animationCenterX, animationCenterY, true)
        }
    }

    private fun showParentWithoutAnimation() {
        val intent = NavUtils.getParentActivityIntent(this)
        intent?.let {
            it.putExtra(MainActivity.IS_SEARCH_SPEAKERS_CLOSED_KEY, true)
            NavUtils.navigateUpTo(this, it)
        }

        turnOffActivityClosingAnimation()
    }

    private fun turnOffActivityClosingAnimation() {
        overridePendingTransition(0, 0)
    }

    private fun haveCircularRevealExtras() =
            intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)

    private fun consume(func: () -> Unit): Boolean {
        func()
        return true
    }

    override fun onDestroy() {
        speakersDisposable.dispose()
        navigationDisposable.dispose()
        super.onDestroy()
    }
}