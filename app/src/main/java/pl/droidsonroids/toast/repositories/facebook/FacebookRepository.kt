package pl.droidsonroids.toast.repositories.facebook

import io.reactivex.Completable
import io.reactivex.Single
import pl.droidsonroids.toast.data.enums.AttendStatus

interface FacebookRepository {
    fun getEventAttendState(eventId: String): Single<AttendStatus>

    fun setEventAttending(eventId: String): Completable
}