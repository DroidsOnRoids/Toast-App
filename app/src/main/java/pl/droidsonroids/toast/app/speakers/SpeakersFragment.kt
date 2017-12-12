package pl.droidsonroids.toast.app.speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.utils.Consts.SEARCH_ITEM_ANIM_DURATION
import pl.droidsonroids.toast.utils.Consts.SEARCH_ITEM_HIDDEN_OFFSET
import pl.droidsonroids.toast.utils.Consts.SEARCH_ITEM_SHOWN_OFFSET

class SpeakersFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_speakers, container, false)


    override fun onResume() {
        super.onResume()
        showSearchMenuItemWithAnimation()
    }

    private fun showSearchMenuItemWithAnimation() {
        animateViewByY(SEARCH_ITEM_SHOWN_OFFSET)
    }

    private fun hideSearchMenuItemWithAnimation() {
        animateViewByY(SEARCH_ITEM_HIDDEN_OFFSET)
    }

    private fun animateViewByY(offset: Float) {
        activity?.run {
            findViewById<View>(R.id.menuItemSearch)
                    .animate()
                    .y(offset)
                    .setDuration(SEARCH_ITEM_ANIM_DURATION)
                    .start()
        }
    }

    override fun onPause() {
        super.onPause()
        hideSearchMenuItemWithAnimation()
    }

}