package pl.droidsonroids.toast.app.notifications

import android.os.Bundle
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import io.reactivex.Observable
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.services.NotificationStorage
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.Constants.Notifications.MIN_TIME_TO_SET_REMINDER_MS
import pl.droidsonroids.toast.utils.StringProvider
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalNotificationScheduler @Inject constructor(
        private val jobDispatcher: FirebaseJobDispatcher,
        private val notificationStorage: NotificationStorage,
        private val stringProvider: StringProvider
) {

    fun scheduleNotification(id: Long, title: String, date: Date, notificationReminderShift: Long): Boolean {
        val bundle = Bundle().apply {
            putString(Constants.Notifications.TITLE, title)
            putLong(Constants.Notifications.ID, id)
            putLong(Constants.Notifications.DATE, date.time)
        }
        val startTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(date.time - Date().time
                - notificationReminderShift).toInt()
        if (startTimeSeconds > 0) {
            val executionWindowDeltaInSeconds = TimeUnit.SECONDS.toMillis(MIN_TIME_TO_SET_REMINDER_MS).toInt()
            val job = jobDispatcher.newJobBuilder()
                    .setTag(id.toString())
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(
                            Trigger.executionWindow(
                                    startTimeSeconds - executionWindowDeltaInSeconds,
                                    startTimeSeconds + executionWindowDeltaInSeconds
                            )
                    )
                    .setService(LocalNotificationService::class.java)
                    .setExtras(bundle)
                    .setReplaceCurrent(true)
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

    val reminderOptions
        get() = stringProvider.getStringArray(R.array.event_reminder_time)
                .zip(stringProvider.getStringArray(R.array.event_reminder_time_values))
                .map { (option, value) -> option to TimeUnit.HOURS.toMillis(value.toLong()) }
}