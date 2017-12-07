package pl.droidsonrioids.toast.app.home

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.app.base.BaseActivity


const val EVENTS_TAB_INDEX = 0
const val SPEAKERS_TAB_INDEX = 1
const val CONTACT_TAB_INDEX = 2


const val INFO_DIALOG_TAG = "info_dialog_tag"

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupViewPager()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupViewPager() {
        homeViewPager.adapter = HomePagerAdapter(supportFragmentManager)
        with(homeTabLayout) {
            setupWithViewPager(homeViewPager)
            getTabAt(EVENTS_TAB_INDEX)?.setIcon(R.drawable.ic_tab_events_selector)
            getTabAt(SPEAKERS_TAB_INDEX)?.setIcon(R.drawable.ic_tab_lecturers_selector)
            getTabAt(CONTACT_TAB_INDEX)?.setIcon(R.drawable.ic_tab_contact_selector)
        }

        homeViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                when (position) {
                    EVENTS_TAB_INDEX -> homeTitle.text = getString(R.string.events_title)
                    SPEAKERS_TAB_INDEX -> homeTitle.text = getString(R.string.speakers_title)
                    CONTACT_TAB_INDEX -> homeTitle.text = getString(R.string.toast_title)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menuItemAbout -> consume { showInfoDialog() }
                else -> super.onOptionsItemSelected(item)
            }

    private fun showInfoDialog() {
        InfoDialogFragment().show(supportFragmentManager, INFO_DIALOG_TAG)
    }

    private fun consume(func: () -> Unit): Boolean {
        func()
        return true
    }

}

