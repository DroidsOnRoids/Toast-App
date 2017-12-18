package pl.droidsonroids.toast.app

import android.content.Context
import pl.droidsonroids.toast.app.speakers.SpeakersSearchActivity
import pl.droidsonroids.toast.utils.NavigationRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    fun dispatch(context: Context, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakersSearch -> showSpeakersSearch(context)
        }
    }

    private fun showSpeakersSearch(context: Context) {
        val intent = SpeakersSearchActivity.createIntent(context)
        context.startActivity(intent)
    }
}