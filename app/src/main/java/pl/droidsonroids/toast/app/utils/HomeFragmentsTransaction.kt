package pl.droidsonroids.toast.app.utils

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.contact.ContactFragment
import pl.droidsonroids.toast.app.events.EventsFragment
import pl.droidsonroids.toast.app.home.InfoDialogFragment
import pl.droidsonroids.toast.app.speakers.SpeakersFragment

private const val EVENTS_FRAGMENT_TAG = "events_fragment_tag"
private const val SPEAKERS_FRAGMENT_TAG = "speakers_fragment_tag"
private const val CONTACT_FRAGMENT_TAG = "contact_fragment_tag"
private const val INFO_DIALOG_TAG = "info_dialog_tag"

class HomeFragmentsTransaction(private val supportFragmentManager: FragmentManager) {

    private var currentFragment: Fragment? = null


    fun showEventsFragment() {
        showFragment(EVENTS_FRAGMENT_TAG) {
            EventsFragment()
        }
    }

    fun showSpeakersFragment() {
        showFragment(SPEAKERS_FRAGMENT_TAG) {
            SpeakersFragment()
        }
    }

    fun showContactFragment() {
        showFragment(CONTACT_FRAGMENT_TAG) {
            ContactFragment()
        }
    }

    private fun showFragment(fragmentTag: String, fragmentCreator: () -> Fragment) {
        supportFragmentManager.beginTransaction {
            supportFragmentManager.findFragmentByTag(fragmentTag)?.let {
                replaceFragment(it)
            } ?: addFragment(fragmentCreator(), fragmentTag)
        }
    }

    @SuppressLint("CommitTransaction")
    private fun FragmentManager.beginTransaction(transaction: FragmentTransaction.() -> Unit) {
        with(beginTransaction()) {
            transaction()
            commit()
        }
    }

    private fun FragmentTransaction.replaceFragment(fragmentToReplace: Fragment) {
        if (!fragmentToReplace.isVisible) {
            setCustomAnimations(R.anim.animation_translated_cross_fade_in, R.anim.animation_cross_fade_out)
            currentFragment?.let { detach(it) }
            attach(fragmentToReplace)
            currentFragment = fragmentToReplace
        }
    }

    private fun FragmentTransaction.addFragment(fragment: Fragment, fragmentTag: String) {
        setCustomAnimations(R.anim.animation_translated_cross_fade_in, R.anim.animation_cross_fade_out)
        currentFragment?.let { detach(it) }
        add(R.id.fragmentContainer, fragment, fragmentTag)
        currentFragment = fragment
    }

    fun showInfoDialog() {
        InfoDialogFragment().show(supportFragmentManager, INFO_DIALOG_TAG)
    }
}