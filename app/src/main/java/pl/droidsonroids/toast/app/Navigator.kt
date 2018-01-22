package pl.droidsonroids.toast.app

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.events.TalkDetailsActivity
import pl.droidsonroids.toast.app.photos.PhotosActivity
import pl.droidsonroids.toast.app.speakers.SpeakerDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakersSearchActivity
import pl.droidsonroids.toast.app.utils.disableActivityTransitionAnimations
import pl.droidsonroids.toast.utils.NavigationRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    fun dispatch(context: Context, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakerDetails -> showSpeakerDetails(context, navigationRequest)
            is NavigationRequest.EventDetails -> showEventDetails(context, navigationRequest)
            is NavigationRequest.Photos -> showPhotos(context, navigationRequest)
        }
    }

    fun showTalkDetailsWithSharedAnimation(activity: AppCompatActivity, navigationRequest: NavigationRequest.TalkDetails, sharedViews: Array<Pair<View, String>>) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedViews).toBundle()
        val intent = TalkDetailsActivity.createIntent(activity, navigationRequest)
        activity.startActivity(intent, options)
    }

    fun showSearchSpeakersWithRevealAnimation(activity: Activity, centerCoordinates: kotlin.Pair<Int, Int>) {
        val intent = SpeakersSearchActivity.createIntent(
                activity,
                revealCenterX = centerCoordinates.first,
                revealCenterY = centerCoordinates.second)

        activity.startActivity(intent)
        activity.disableActivityTransitionAnimations()
    }

    private fun showEventDetails(context: Context, navigationRequest: NavigationRequest.EventDetails) {
        val intent = EventDetailsActivity.createIntent(context, navigationRequest)
        context.startActivity(intent)
    }

    private fun showSpeakerDetails(context: Context, navigationRequest: NavigationRequest.SpeakerDetails) {
        val intent = SpeakerDetailsActivity.createIntent(context, navigationRequest)
        context.startActivity(intent)
    }

    private fun showPhotos(context: Context, navigationRequest: NavigationRequest.Photos) {
        val intent = PhotosActivity.createIntent(context, navigationRequest)
        context.startActivity(intent)
    }
}