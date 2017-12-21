package pl.droidsonroids.toast.app

import android.app.Activity
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.widget.ImageButton
import pl.droidsonroids.toast.app.speakers.SpeakersSearchActivity
import pl.droidsonroids.toast.utils.NavigationRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    fun dispatch(activity: Activity, searchIcon: ImageButton, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakersSearch -> showSpeakersSearchWithAnimation(activity, searchIcon)
        }
    }

    private fun showSpeakersSearchWithAnimation(activity: Activity, searchIcon: ImageButton) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, searchIcon, "transition")
        val revealX = (searchIcon.x + searchIcon.width / 2).toInt()
        val revealY = (searchIcon.y + searchIcon.height / 2).toInt()

        val intent = SpeakersSearchActivity.createIntent(activity)
        intent.putExtra(SpeakersSearchActivity.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(SpeakersSearchActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY)

        ActivityCompat.startActivity(activity, intent, options.toBundle())
    }
}