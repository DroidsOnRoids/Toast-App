package pl.droidsonroids.toast.app.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.utils.MainCategories
import pl.droidsonroids.toast.databinding.ActivityMainBinding
import pl.droidsonroids.toast.utils.Constants.SearchMenuItem.ANIM_DURATION_MILLIS
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.MainViewModel
import javax.inject.Inject


class MainActivity : BaseActivity() {

    companion object {
        private const val CURRENT_TITLE = "current_title"
        private const val MAIN_CATEGORIES_KEY = "main_categories_key"

        fun createIntent(context: Context, mainCategories: MainCategories): Intent {
            return Intent(context, MainActivity::class.java)
                    .putExtra(MAIN_CATEGORIES_KEY, mainCategories)
        }
    }

    private lateinit var homeFragmentTransaction: HomeFragmentsTransaction
    private var navigationDisposable: Disposable? = null
    private val mainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
    }

    @Inject
    lateinit var navigator: Navigator

    private val homeScreen by lazy {
        intent.getSerializableExtra(MAIN_CATEGORIES_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setupToolbar(savedInstanceState)
        setupNavigationView()
        initHomeFragmentTransaction(showEventsFragment = savedInstanceState == null)

        setupViewModel(mainBinding)
        showHomeScreen()
    }

    private fun showHomeScreen() {
        when (homeScreen) {
            MainCategories.EVENTS -> homeFragmentTransaction.showEventsFragment()
            MainCategories.SPEAKERS -> homeFragmentTransaction.showSpeakersFragment()
            MainCategories.CONTACTS -> homeFragmentTransaction.showContactFragment()
        }
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
                .setDuration(ANIM_DURATION_MILLIS)
                .start()
    }

    private fun setupToolbar(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        savedInstanceState?.let {
            homeTitle.text = it.getString(CURRENT_TITLE)
        }
    }

    private fun setupNavigationView() {
        setHomeNavigationItemReselectedListener()
        setHomeNavigationItemSelectedListener()
    }

    private fun initHomeFragmentTransaction(showEventsFragment: Boolean) {
        homeFragmentTransaction = HomeFragmentsTransaction(supportFragmentManager)
        if (showEventsFragment) {
            homeFragmentTransaction.showEventsFragment()
        }
    }

    private fun setupViewModel(mainBinding: ActivityMainBinding) {
        mainBinding.mainViewModel = mainViewModel
        navigationDisposable = mainViewModel.navigationSubject
                .subscribe {
                    navigator.showSearchSpeakersWithRevealAnimation(
                            activity = this,
                            centerCoordinates = getViewCenterCoordinates(searchImageButton))
                }
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

    private fun getViewCenterCoordinates(view: View): Pair<Int, Int> {
        val centerX = (view.x + view.width / 2).toInt()
        val centerY = (view.y + view.height / 2).toInt()
        return centerX to centerY
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_TITLE, homeTitle.text.toString())
    }

    override fun onDestroy() {
        navigationDisposable?.dispose()
        super.onDestroy()
    }

}

