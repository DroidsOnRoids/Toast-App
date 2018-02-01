package pl.droidsonroids.toast.repositories.facebook

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.facebook.UserManager
import pl.droidsonroids.toast.data.UserInfo
import pl.droidsonroids.toast.data.api.facebook.ApiUserAttend
import pl.droidsonroids.toast.data.api.facebook.FacebookAttendResponse
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.services.FacebookService

class FacebookRepositoryImplTest : RxTestBase() {
    @Mock
    private lateinit var facebookService: FacebookService
    @Mock
    private lateinit var userManager: UserManager
    @InjectMocks
    private lateinit var facebookRepositoryImpl: FacebookRepositoryImpl

    private val userId = "userId"
    private val token = "token"
    private val bearer = "Bearer $token"
    private val eventId = "1"

    @Before
    fun setUp() {
        whenever(userManager.getUserInfo()).thenReturn(UserInfo(token, userId))
    }

    @Test
    fun shouldReturnAttendingState() {
        mockServiceResponses(attendingStatus = AttendStatus.ATTENDING)

        facebookRepositoryImpl.getEventAttendState(eventId)
                .test()
                .assertValue { it == AttendStatus.ATTENDING }
    }

    @Test
    fun shouldReturnUnsureState() {
        mockServiceResponses(interestedStatus = AttendStatus.UNSURE)

        facebookRepositoryImpl.getEventAttendState(eventId)
                .test()
                .assertValue { it == AttendStatus.UNSURE }
    }

    @Test
    fun shouldReturnDeclinedState() {
        mockServiceResponses()

        facebookRepositoryImpl.getEventAttendState(eventId)
                .test()
                .assertValue { it == AttendStatus.DECLINED }
    }

    @Test
    fun shouldReturnDeclinedStateWhenNotLoggedIn() {
        whenever(userManager.getUserInfo()).thenReturn(null)

        facebookRepositoryImpl.getEventAttendState(eventId)
                .test()
                .assertValue { it == AttendStatus.DECLINED }
    }

    private fun mockServiceResponses(attendingStatus: AttendStatus? = null, interestedStatus: AttendStatus? = null) {
        whenever(facebookService.getEventAttendingState(eq(bearer), eq(eventId), eq(userId))).thenReturn(Single.just(createAttendResponse(attendingStatus)))
        whenever(facebookService.getEventInterestedState(eq(bearer), eq(eventId), eq(userId))).thenReturn(Single.just(createAttendResponse(interestedStatus)))
    }

    private fun createAttendResponse(attendStatus: AttendStatus?) = FacebookAttendResponse(
            listOfNotNull(
                    attendStatus?.let { ApiUserAttend(it) }
            )
    )

}
