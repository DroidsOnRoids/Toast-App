package pl.droidsonroids.toast.app

import android.app.Activity
import android.content.Context
import android.view.View
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakerDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakersSearchActivity
import pl.droidsonroids.toast.app.utils.turnOffActivityClosingAnimation
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
        val revealCenterX = (animatedView.x + animatedView.width / 2).toInt()
        val revealCenterY = (animatedView.y + animatedView.height / 2).toInt()

        val intent = SpeakersSearchActivity.createIntent(activity, revealCenterX, revealCenterY)
        activity.startActivity(intent)
        activity.turnOffActivityClosingAnimation()
    }
}