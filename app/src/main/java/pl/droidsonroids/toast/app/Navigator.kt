package pl.droidsonroids.toast.app

import android.app.AlertDialog
import android.content.Context
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import pl.droidsonroids.toast.R
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.events.TalkDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakerDetailsActivity
import pl.droidsonroids.toast.app.speakers.SpeakersSearchActivity
import pl.droidsonroids.toast.utils.NavigationRequest
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

const val MESSAGE_SENT_AUTO_DISMISS_TIME = 2L

@Singleton
class Navigator @Inject constructor() {
    fun dispatch(context: Context, navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            is NavigationRequest.SpeakersSearch -> showSpeakersSearch(context)
            is NavigationRequest.SpeakerDetails -> showSpeakerDetails(context, navigationRequest)
            is NavigationRequest.EventDetails -> showEventDetails(context, navigationRequest)
            is NavigationRequest.MessageSent -> showMessageSentDialog(context)
        }
    }
  
    private fun showMessageSentDialog(context: Context) {
        var timerDisposable = Disposables.disposed()
        AlertDialog.Builder(context)
                .setView(R.layout.layout_message_sent)
                .setOnDismissListener { timerDisposable.dispose() }
                .create()
                .run {
                    timerDisposable = startDismissTimer(this)
                    show()
                }
    }

    private fun startDismissTimer(dialog: AlertDialog): Disposable {
        return Completable.timer(MESSAGE_SENT_AUTO_DISMISS_TIME, TimeUnit.SECONDS)
                .subscribe(dialog::dismiss)
    }
      
    fun showTalkDetailsWithSharedAnimation(activity: AppCompatActivity, navigationRequest: NavigationRequest.TalkDetails, sharedViews: Array<Pair<View, String>>) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedViews).toBundle()
        val intent = TalkDetailsActivity.createIntent(activity, navigationRequest)
        activity.startActivity(intent, options)
    }

    private fun showEventDetails(context: Context, navigationRequest: NavigationRequest.EventDetails) {
        val intent = EventDetailsActivity.createIntent(context, navigationRequest)
        context.startActivity(intent)
    }

    private fun showSpeakerDetails(context: Context, navigationRequest: NavigationRequest.SpeakerDetails) {
        val intent = SpeakerDetailsActivity.createIntent(context, navigationRequest)
        context.startActivity(intent)
    }

    private fun showSpeakersSearch(context: Context) {
        val intent = SpeakersSearchActivity.createIntent(context)
        context.startActivity(intent)
    }
}