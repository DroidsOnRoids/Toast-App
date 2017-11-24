package pl.droidsonrioids.toast.app.home

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import pl.droidsonrioids.toast.app.events.EventsFragment

const val HOME_TABS_COUNT = 3

class HomePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    // TODO: TOA-18, TOA-20 Add proper fragments
    override fun getItem(position: Int) = EventsFragment()

    override fun getCount() = HOME_TABS_COUNT
}