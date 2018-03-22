package pl.droidsonroids.toast.app

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.facebook.login.LoginManager
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
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

    fun dispatch(baseActivity: BaseActivity, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakerDetails -> showSpeakerDetails(baseActivity, navigationRequest)
            is NavigationRequest.EventDetails -> showEventDetails(baseActivity, navigationRequest)
            is NavigationRequest.Photos -> showPhotos(baseActivity, navigationRequest)
            is NavigationRequest.Map -> showMap(baseActivity, navigationRequest)
            is NavigationRequest.Website -> openWebsite(baseActivity, navigationRequest.url)
            is NavigationRequest.Email -> openEmailClient(baseActivity, navigationRequest.email)
            is NavigationRequest.SnackBar -> baseActivity.showSnackbar(navigationRequest)
            NavigationRequest.LogIn -> logIn(baseActivity)
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

    private fun showMap(baseActivity: BaseActivity, navigationRequest: NavigationRequest.Map) {
        with(navigationRequest) {
            val locationUri = Uri.parse("geo:0,0?q=${coordinatesDto.latitude},${coordinatesDto.longitude}($placeName)")
            val intent = Intent(Intent.ACTION_VIEW).setData(locationUri)
            try {
                baseActivity.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                val query = URLEncoder.encode("${coordinatesDto.latitude},${coordinatesDto.longitude}", Charsets.UTF_8.name())
                openWebsite(baseActivity, "https://www.google.com/maps/search/?api=1&query=$query")
            }
        }
    }

    private fun openEmailClient(baseActivity: BaseActivity, email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$email")
            baseActivity.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            baseActivity.copyTextToClipboard(Constants.ClipDataLabel.EMAIL, email)
            baseActivity.showSnackbar(NavigationRequest.SnackBar(R.string.email_is_copied_to_clipboard)).takeIf { it }
                    ?: Toast.makeText(baseActivity, R.string.email_is_copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWebsite(baseActivity: BaseActivity, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            baseActivity.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            baseActivity.showSnackbar(NavigationRequest.SnackBar(R.string.error_internet_browser_not_found)).takeIf { it }
                    ?: Toast.makeText(baseActivity, R.string.error_internet_browser_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    fun showActivityWithSharedAnimation(activity: AppCompatActivity, navigationRequest: NavigationRequest, sharedViews: Array<Pair<View, String>>) {
        if (activity.hasWindowFocus()) {
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
    }

    fun showSearchSpeakersWithRevealAnimation(baseActivity: BaseActivity, centerCoordinates: kotlin.Pair<Int, Int>) {
        val intent = SpeakersSearchActivity.createIntent(
                baseActivity,
                revealCenterX = centerCoordinates.first,
                revealCenterY = centerCoordinates.second)

        baseActivity.startActivity(intent)
        baseActivity.disableActivityTransitionAnimations()
        analyticsEventTracker.logSpeakersShowSearchEvent()
    }

    private fun showEventDetails(baseActivity: BaseActivity, navigationRequest: NavigationRequest.EventDetails) {
        val intent = EventDetailsActivity.createIntent(baseActivity, navigationRequest)
        baseActivity.startActivity(intent)
    }

    private fun showSpeakerDetails(baseActivity: BaseActivity, navigationRequest: NavigationRequest.SpeakerDetails) {
        val intent = SpeakerDetailsActivity.createIntent(baseActivity, navigationRequest)
        baseActivity.startActivity(intent)
    }

    private fun showPhotos(baseActivity: BaseActivity, navigationRequest: NavigationRequest.Photos) {
        val intent = PhotosActivity.createIntent(baseActivity, navigationRequest)
        baseActivity.startActivity(intent)
    }
}