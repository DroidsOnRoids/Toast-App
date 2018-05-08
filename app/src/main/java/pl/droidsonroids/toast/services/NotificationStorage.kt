package pl.droidsonroids.toast.services

import io.reactivex.Observable

interface NotificationStorage {

    fun setIsNotificationScheduled(eventId: Long, isScheduled: Boolean)
    fun getIsNotificationScheduled(eventId: Long): Observable<Boolean>
}
