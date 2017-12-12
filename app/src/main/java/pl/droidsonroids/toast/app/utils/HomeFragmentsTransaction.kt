package pl.droidsonroids.toast.app.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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

    init {
        showEventsFragment()
    }


    fun showEventsFragment() {
        showFragmentWithAnimation(EVENTS_FRAGMENT_TAG) {
            EventsFragment()
        }
    }

    fun showSpeakersFragment() {
        showFragmentWithAnimation(SPEAKERS_FRAGMENT_TAG) {
            SpeakersFragment()
        }
    }

    fun showContactFragment() {
        showFragmentWithAnimation(CONTACT_FRAGMENT_TAG) {
            ContactFragment()
        }
    }

    private fun showFragmentWithAnimation(fragmentTag: String, fragmentCreator: () -> Fragment) {
        supportFragmentManager.beginTransaction {
            setCustomAnimations(R.anim.animation_translated_cross_fade_in, R.anim.animation_cross_fade_out)

            currentFragment?.let { detach(it) }

            val fragmentToReplace = supportFragmentManager.findFragmentByTag(fragmentTag)
            fragmentToReplace?.let {
                attach(it)
                currentFragment = it
            } ?: run {
                val newFragment = fragmentCreator()
                add(R.id.fragmentContainer, newFragment, fragmentTag)
                currentFragment = newFragment
            }
        }
    }

    fun showInfoDialog() {
        InfoDialogFragment().show(supportFragmentManager, INFO_DIALOG_TAG)
    }
}