package pl.droidsonrioids.toast.app.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

const val HOME_TABS_COUNT = 3

class HomePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    // TODO: 21/11/2017 Add proper fragments
    override fun getItem(position: Int): Fragment = Fragment()

    override fun getCount() = HOME_TABS_COUNT
}