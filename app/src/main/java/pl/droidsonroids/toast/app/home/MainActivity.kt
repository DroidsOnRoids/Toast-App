package pl.droidsonroids.toast.app.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.Constants.SEARCH_ITEM_HIDDEN_OFFSET
import pl.droidsonroids.toast.viewmodels.MainViewModel
import javax.inject.Inject


class MainActivity : BaseActivity() {

    private lateinit var homeFragmentTransaction: HomeFragmentsTransaction

    @Inject
    lateinit var navigator: Navigator

    private val mainViewModel by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    private var navigationDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState?.clear()

        setupToolbar()
        setupNavigationView()
        initHomeFragmentTransaction()
        initSpeakersSearchButton()

        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menuItemAbout -> consume { homeFragmentTransaction.showInfoDialog() }
                else -> super.onOptionsItemSelected(item)
            }

    fun animateSearchButton(offset: Float) {
        searchImageButton
                .animate()
                .y(offset)
                .setDuration(Constants.SEARCH_ITEM_ANIM_DURATION_MILLIS)
                .start()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupNavigationView() {
        setHomeNavigationItemReselectedListener()
        setHomeNavigationItemSelectedListener()
    }

    private fun setupViewModel() {
        navigationDisposable = mainViewModel.navigationSubject
                .subscribe { navigator.dispatch(this, it) }
    }

    private fun setHomeNavigationItemSelectedListener() {
        homeNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.actionEvents -> {
                    homeFragmentTransaction.showEventsFragment()
                    setHomeTitleText(R.string.events_title)
                }
                R.id.actionSpeakers -> {
                    homeFragmentTransaction.showSpeakersFragment()
                    setHomeTitleText(R.string.speakers_title)
                }
                R.id.actionContact -> {
                    homeFragmentTransaction.showContactFragment()
                    setHomeTitleText(R.string.contact_title)
                }
            }
            true
        }
    }

    private fun setHomeNavigationItemReselectedListener() {
        homeNavigationView.setOnNavigationItemReselectedListener {
            //TODO: Scroll the fragment's content up
        }
    }

    private fun setHomeTitleText(titleRes: Int) {
        homeTitle.text = getText(titleRes)
    }

    private fun initHomeFragmentTransaction() {
        homeFragmentTransaction = HomeFragmentsTransaction(supportFragmentManager)
    }

    private fun initSpeakersSearchButton() {
        searchImageButton.apply {
            translationY = SEARCH_ITEM_HIDDEN_OFFSET
            setOnClickListener { mainViewModel.onSpeakerSearchRequested() }
        }
    }

    override fun onDestroy() {
        navigationDisposable?.dispose()
        super.onDestroy()
    }

    private fun consume(func: () -> Unit): Boolean {
        func()
        return true
    }

}

