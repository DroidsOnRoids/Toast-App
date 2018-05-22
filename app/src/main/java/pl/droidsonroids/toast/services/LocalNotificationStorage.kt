package pl.droidsonroids.toast.services

import android.content.SharedPreferences
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

private const val NOTIFICATION_KEY_PREFIX = "notification"

@Singleton
class LocalNotificationStorage @Inject constructor(
        private val sharedPreferences: SharedPreferences
) : NotificationStorage, SharedPreferences.OnSharedPreferenceChangeListener {
    private val listeners = mutableMapOf<String, (Boolean) -> Unit>()

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun setIsNotificationScheduled(eventId: Long, isScheduled: Boolean) {
        sharedPreferences.edit()
                .putBoolean("$NOTIFICATION_KEY_PREFIX$eventId", isScheduled)
                .apply()
    }

    override fun getIsNotificationScheduled(eventId: Long): Observable<Boolean> {
        val key = "$NOTIFICATION_KEY_PREFIX$eventId"
        return Observable.create { emitter ->
            listeners[key] = emitter::onNext
            emitter.setCancellable { listeners.remove(key) }
            emitter.onNext(sharedPreferences.getBoolean(key, false))
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        listeners[key]?.invoke(sharedPreferences.getBoolean(key, false))
    }
}