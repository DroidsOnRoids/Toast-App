package pl.droidsonroids.toast.app.notifications

import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import dagger.android.AndroidInjection
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.events.EventDetailsActivity.Companion.EVENT_ID
import pl.droidsonroids.toast.services.NotificationStorage
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import java.util.*
import javax.inject.Inject

class LocalNotificationService : JobService() {

    @Inject
    lateinit var notificationStorage: NotificationStorage

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStopJob(job: JobParameters): Boolean {
        return false
    }

    override fun onStartJob(job: JobParameters): Boolean {
        job.extras?.run {
            val id = getLong(Constants.Notifications.ID)
            val title = getString(Constants.Notifications.TITLE)
            val date = getLong(Constants.Notifications.DATE).let { Date(it) }
            showNotification(id, title, date)
        }
        return false
    }

    private fun showNotification(id: Long, title: String?, date: Date?) {
        val navigationRequest = NavigationRequest.EventDetails(id)
        val intent = EventDetailsActivity.createIntent(this, navigationRequest)
                .putExtra(EVENT_ID, id.toString())
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val channelId = resources.getString(R.string.default_notification_channel_id)
        val notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo_toast)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(resources.getString(R.string.event_reminder_description, title, date))
                .setAutoCancel(true)
                .build()
        NotificationManagerCompat.from(this).notify(id.toInt(), notification)
        notificationStorage.setIsNotificationScheduled(id, false)
    }
}