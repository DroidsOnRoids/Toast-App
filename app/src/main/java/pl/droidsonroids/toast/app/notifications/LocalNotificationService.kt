package pl.droidsonroids.toast.app.notifications

import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import dagger.android.AndroidInjection
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.events.EventDetailsActivity
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
        val id = job.extras?.getLong(Constants.Notifications.ID) ?: 0
        val title = job.extras?.getString(Constants.Notifications.TITLE)
        val date = job.extras?.getLong(Constants.Notifications.DATE)?.let { Date(it) }
        val navigationRequest = NavigationRequest.EventDetails(id)
        val intent = EventDetailsActivity.createIntent(this, navigationRequest)
                .putExtra("event_id", id.toString())
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, resources.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_logo_toast)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(resources.getString(R.string.event_reminder_description, title, date))
                .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(id.toInt(), builder.build())
        notificationStorage.setIsNotificationScheduled(id, false)
        return false
    }
}