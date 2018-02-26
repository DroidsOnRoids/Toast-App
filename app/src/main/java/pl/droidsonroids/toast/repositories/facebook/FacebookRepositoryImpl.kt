package pl.droidsonroids.toast.repositories.facebook

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import pl.droidsonroids.toast.app.facebook.UserManager
import pl.droidsonroids.toast.data.api.facebook.FacebookAttendResponse
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.services.FacebookService
import pl.droidsonroids.toast.utils.UserNotLoggedInException
import javax.inject.Inject

class FacebookRepositoryImpl @Inject constructor(private val facebookService: FacebookService, private val userManager: UserManager) : FacebookRepository {
    override fun getEventAttendState(eventId: String): Single<AttendStatus> {
        return userManager.getUserInfo()?.run {
            Single.zip(
                    facebookService.getEventAttendingState(token, eventId, userId)
                            .mapToAttendStatus()
                            .onErrorReturnItem(AttendStatus.ERROR),
                    facebookService.getEventInterestedState(token, eventId, userId)
                            .mapToAttendStatus()
                            .onErrorReturnItem(AttendStatus.ERROR),
                    BiFunction<AttendStatus, AttendStatus, AttendStatus> { attendStatus, interestedStatus ->
                        when {
                            attendStatus == AttendStatus.ATTENDING -> AttendStatus.ATTENDING
                            interestedStatus == AttendStatus.UNSURE -> AttendStatus.UNSURE
                            attendStatus == AttendStatus.DECLINED
                                    && interestedStatus == AttendStatus.DECLINED -> AttendStatus.DECLINED
                            else -> AttendStatus.ERROR
                        }
                    }
            )
        } ?: Single.just(AttendStatus.DECLINED)

    }

    private fun Single<FacebookAttendResponse>.mapToAttendStatus() =
            map { it.data.firstOrNull()?.attendStatus ?: AttendStatus.DECLINED }

    override fun setEventAttending(eventId: String): Completable {
        return userManager.getUserInfo()?.run {
            facebookService.postEventAttending(token, eventId)
        } ?: Completable.error(UserNotLoggedInException())
    }
}