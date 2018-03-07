package pl.droidsonroids.toast.app

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.facebook.login.LoginManager
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.events.EventTalkDetailsActivity
import pl.droidsonroids.toast.app.photos.PhotosActivity
import pl.droidsonroids.toast.app.speakers.SpeakerDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakerTalkDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakersSearchActivity
import pl.droidsonroids.toast.app.utils.extensions.copyTextToClipboard
import pl.droidsonroids.toast.app.utils.extensions.disableActivityTransitionAnimations
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import java.lang.IllegalArgumentException
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val loginManager: LoginManager, private val analyticsEventTracker: AnalyticsEventTracker) {

    fun dispatch(activity: Activity, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakerDetails -> showSpeakerDetails(activity, navigationRequest)
            is NavigationRequest.EventDetails -> showEventDetails(activity, navigationRequest)
            is NavigationRequest.Photos -> showPhotos(activity, navigationRequest)
            is NavigationRequest.Map -> showMap(activity, navigationRequest)
            is NavigationRequest.Website -> openWebsite(activity, navigationRequest.url)
            is NavigationRequest.Email -> openEmailClient(activity, navigationRequest.email)
            NavigationRequest.LogIn -> logIn(activity)
            NavigationRequest.LogOut -> logOut()
        }
    }

    // Not used for now due to graph API change - removed possibility to check attend status
    private fun logIn(activity: Activity) {
        //        loginManager.logInWithPublishPermissions(activity, Constants.Facebook.PERMISSIONS)
    }

    private fun logOut() {
        //        loginManager.logOut()
    }

    private fun showMap(context: Context, navigationRequest: NavigationRequest.Map) {
        with(navigationRequest) {
            val locationUri = Uri.parse("geo:0,0?q=${coordinatesDto.latitude},${coordinatesDto.longitude}($placeName)")
            val intent = Intent(Intent.ACTION_VIEW).setData(locationUri)
            try {
                context.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                val query = URLEncoder.encode("${coordinatesDto.latitude},${coordinatesDto.longitude}", Charsets.UTF_8.name())
                openWebsite(context, "https://www.google.com/maps/search/?api=1&query=$query")
            }
        }
    }

    private fun openEmailClient(context: Context, email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$email")
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            context.copyTextToClipboard(Constants.ClipDataLabel.EMAIL, email)
            Toast.makeText(context, R.string.email_is_copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWebsite(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(context, R.string.browser_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    fun showActivityWithSharedAnimation(activity: AppCompatActivity, navigationRequest: NavigationRequest, sharedViews: Array<Pair<View, String>>) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedViews).toBundle()
        val intent = when (navigationRequest) {
            is NavigationRequest.EventTalkDetails -> EventTalkDetailsActivity.createIntent(activity, navigationRequest)
            is NavigationRequest.SpeakerTalkDetails -> SpeakerTalkDetailsActivity.createIntent(activity, navigationRequest)
            is NavigationRequest.EventDetails -> EventDetailsActivity.createIntent(activity, navigationRequest)
            is NavigationRequest.SpeakerDetails -> SpeakerDetailsActivity.createIntent(activity, navigationRequest)
            else -> throw IllegalArgumentException("The $navigationRequest is not supported.")
        }
        activity.startActivity(intent, options)
    }

    fun showSearchSpeakersWithRevealAnimation(activity: Activity, centerCoordinates: kotlin.Pair<Int, Int>) {
        val intent = SpeakersSearchActivity.createIntent(
                activity,
                revealCenterX = centerCoordinates.first,
                revealCenterY = centerCoordinates.second)

        activity.startActivity(intent)
        activity.disableActivityTransitionAnimations()
        analyticsEventTracker.logSpeakersShowSearchEvent()
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