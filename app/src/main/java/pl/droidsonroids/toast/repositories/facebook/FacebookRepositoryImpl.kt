package pl.droidsonroids.toast.repositories.facebook

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import pl.droidsonroids.toast.app.facebook.UserManager
import pl.droidsonroids.toast.data.api.facebook.FacebookAttendResponse
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.services.FacebookService
import javax.inject.Inject

class FacebookRepositoryImpl @Inject constructor(private val facebookService: FacebookService, private val userManager: UserManager) : FacebookRepository {
    override fun getEventAttendState(eventId: String): Single<AttendStatus> {
        return userManager.getUserInfo()?.run {
            val bearer = "Bearer $token"
            Single.zip(
                    facebookService.getEventAttendingState(bearer, eventId, userId)
                            .mapToAttendStatus(),
                    facebookService.getEventInterestedState(bearer, eventId, userId)
                            .mapToAttendStatus(),
                    BiFunction<AttendStatus, AttendStatus, AttendStatus> { attendStatus, interestedStatus ->
                        attendStatus.takeIf { it == AttendStatus.ATTENDING } ?: interestedStatus
                    }
            )
        } ?: Single.just(AttendStatus.DECLINED)

    }

    private fun Single<FacebookAttendResponse>.mapToAttendStatus() =
            map { it.data.firstOrNull()?.attendStatus ?: AttendStatus.DECLINED }
}