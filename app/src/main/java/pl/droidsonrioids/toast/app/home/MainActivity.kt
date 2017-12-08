package pl.droidsonrioids.toast.app.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.app.base.BaseActivity
import pl.droidsonrioids.toast.app.contact.ContactFrafgment
import pl.droidsonrioids.toast.app.events.EventsFragment
import pl.droidsonrioids.toast.app.speakers.SpeakersFragment

const val EVENTS_FRAGMENT_TAG = "events_fragment_tag"
const val SPEAKERS_FRAGMENT_TAG = "speakers_fragment_tag"
const val CONTACT_FRAGMENT_TAG = "contact_fragment_tag"
const val INFO_DIALOG_TAG = "info_dialog_tag"

class MainActivity : BaseActivity() {

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupNavigationView()
        showEventsFragment()
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

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupNavigationView() {
        homeNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.actionEvents -> showEventsFragment()
                R.id.actionSpeakers -> showSpeakersFragment()
                R.id.actionContact -> showContactFragment()
            }
            true
        }
    }

    private fun showEventsFragment() {
        val newLazyEventsFragment = { EventsFragment() }
        showFragment(newLazyEventsFragment, EVENTS_FRAGMENT_TAG)
    }

    private fun showSpeakersFragment() {
        val newLazySpeakersFragment = { SpeakersFragment() }
        showFragment(newLazySpeakersFragment, SPEAKERS_FRAGMENT_TAG)
    }

    private fun showContactFragment() {
        val newLazyContactFragment = { ContactFrafgment() }
        showFragment(newLazyContactFragment, CONTACT_FRAGMENT_TAG)
    }

    private fun showFragment(fragment: () -> Fragment, fragmentTag: String) {
        supportFragmentManager.beginTransaction {
            supportFragmentManager.findFragmentByTag(fragmentTag)?.let {
                replaceFragment(it)
            } ?: run {
                addFragment(fragment(), fragmentTag)
            }
        }
    }

    @SuppressLint("CommitTransaction")
    private fun FragmentManager.beginTransaction(transaction: FragmentTransaction.() -> Unit) {
        with(beginTransaction()) {
            transaction()
            commit()
        }
    }

    private fun FragmentTransaction.replaceFragment(fragment: Fragment) {
        if (currentFragment != fragment) {
            setCustomAnimations(R.anim.animation_cross_fade_in, R.anim.animation_cross_fade_out)
            hide(currentFragment)
            show(fragment)
            currentFragment = fragment
        }
    }

    private fun FragmentTransaction.addFragment(fragment: Fragment, fragmentTag: String) {
        setCustomAnimations(R.anim.animation_cross_fade_in, R.anim.animation_cross_fade_out)
        currentFragment?.let { hide(it) }
        add(R.id.fragmentContainer, fragment, fragmentTag)
        currentFragment = fragment
    }

    private fun showInfoDialog() {
        InfoDialogFragment().show(supportFragmentManager, INFO_DIALOG_TAG)
    }

    private fun consume(func: () -> Unit): Boolean {
        func()
        return true
    }

}

