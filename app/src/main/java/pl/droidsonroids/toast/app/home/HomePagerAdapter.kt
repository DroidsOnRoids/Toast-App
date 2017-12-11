package pl.droidsonroids.toast.app.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import pl.droidsonroids.toast.app.events.EventsFragment
import pl.droidsonroids.toast.app.speakers.SpeakersFragment
import pl.droidsonroids.toast.utils.Consts

class HomePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    // TODO: TOA-20 Add proper fragments
    override fun getItem(position: Int): Fragment = when (position) {
        EVENTS_TAB_INDEX -> EventsFragment()
        else -> SpeakersFragment()
    }

    override fun getCount() = Consts.HOME_TABS_COUNT
}