package pl.droidsonroids.toast.app.notifications

import android.os.Bundle
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import io.reactivex.Observable
import pl.droidsonroids.toast.services.NotificationStorage
import pl.droidsonroids.toast.utils.Constants
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalNotificationScheduler @Inject constructor(
        private val jobDispatcher: FirebaseJobDispatcher,
        private val notificationStorage: NotificationStorage
) {
    val notificationReminderMilisShift
        get() = TimeUnit.HOURS.toMillis(notificationStorage.reminderNotificationHoursShift)

    fun scheduleNotification(id: Long, title: String, date: Date): Boolean {
        val bundle = Bundle().apply {
            putString(Constants.Notifications.TITLE, title)
            putLong(Constants.Notifications.ID, id)
            putLong(Constants.Notifications.DATE, date.time)
        }
        val startTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(date.time - Date().time - notificationReminderMilisShift).toInt()
        if (startTimeSeconds > 0) {
            val job = jobDispatcher.newJobBuilder()
                    .setTag(id.toString())
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(Trigger.executionWindow(TimeUnit.MINUTES.toSeconds(10).toInt(), TimeUnit.MINUTES.toSeconds(15).toInt()))
                    .setService(LocalNotificationService::class.java)
                    .setExtras(bundle)
                    .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                    .build()
            jobDispatcher.mustSchedule(job)
            notificationStorage.setIsNotificationScheduled(id, true)
            return true
        }
        return false
    }

    fun unscheduleNotification(id: Long) {
        jobDispatcher.cancel(id.toString())
        notificationStorage.setIsNotificationScheduled(id, false)
    }

    fun getIsNotificationScheduled(id: Long): Observable<Boolean> {
        return notificationStorage.getIsNotificationScheduled(id)
    }
}