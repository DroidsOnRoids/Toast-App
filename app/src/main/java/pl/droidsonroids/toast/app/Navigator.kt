package pl.droidsonroids.toast.app

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakerDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakersSearchActivity
import pl.droidsonroids.toast.utils.NavigationRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    fun dispatch(context: Context, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakerDetails -> showSpeakerDetails(context, navigationRequest)
            is NavigationRequest.EventDetails -> showEventDetails(context, navigationRequest)
        }
    }

    fun dispatch(activity: Activity, animatedView: View, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakersSearch -> showSpeakersSearchWithRevealAnimation(activity, animatedView)
        }
    }

    private fun showEventDetails(context: Context, navigationRequest: NavigationRequest.EventDetails) {
        val intent = EventDetailsActivity.createIntent(context, navigationRequest)
        context.startActivity(intent)
    }

    private fun showSpeakerDetails(context: Context, navigationRequest: NavigationRequest.SpeakerDetails) {
        val intent = SpeakerDetailsActivity.createIntent(context, navigationRequest)
        context.startActivity(intent)
    }

    private fun showSpeakersSearchWithRevealAnimation(activity: Activity, animatedView: View) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, animatedView, animatedView.transitionName)
        val revealX = (animatedView.x + animatedView.width / 2).toInt()
        val revealY = (animatedView.y + animatedView.height / 2).toInt()

        val intent = SpeakersSearchActivity.createIntent(activity)
        intent.putExtra(SpeakersSearchActivity.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(SpeakersSearchActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY)

        ActivityCompat.startActivity(activity, intent, options.toBundle())
    }
}