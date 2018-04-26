package pl.droidsonroids.toast.services

import android.content.SharedPreferences
import io.reactivex.Observable
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.utils.StringProvider
import javax.inject.Inject
import javax.inject.Singleton

private const val NOTIFICATION_KEY_PREFIX = "notification"

@Singleton
class LocalNotificationStorage @Inject constructor(private val sharedPreferences: SharedPreferences,
                                                   stringProvider: StringProvider) : NotificationStorage {
    private val eventReminderKey = stringProvider.getString(R.string.pref_key_event_reminder_notifications)

    override fun setIsNotificationScheduled(eventId: Long, isScheduled: Boolean) {
        sharedPreferences.edit()
                .putBoolean("$NOTIFICATION_KEY_PREFIX$eventId", isScheduled)
                .apply()
    }

    override fun getIsNotificationScheduled(eventId: Long): Observable<Boolean> {
        val desiredKey = "$NOTIFICATION_KEY_PREFIX$eventId"
        return Observable.create { emitter ->
            val listener = { _: SharedPreferences, key: String -> if (key == desiredKey) emitter.onNext(sharedPreferences.getBoolean(key, false)) }
            emitter.setCancellable {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            emitter.onNext(sharedPreferences.getBoolean(desiredKey, false))
        }
    }

    override val reminderNotificationHoursShift
        get() = sharedPreferences.getString(eventReminderKey, "2").toLong()
}