package pl.droidsonroids.toast.viewmodels.facebook

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.app.utils.managers.FirebaseAnalyticsEventTracker
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.repositories.facebook.FacebookRepository
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest

class FacebookAttendViewModelTest : RxTestBase() {

    @Mock
    private lateinit var loginStateWatcher: LoginStateWatcher
    @Mock
    private lateinit var facebookRepository: FacebookRepository
    @Mock
    private lateinit var firebaseAnalyticsEventTracker: FirebaseAnalyticsEventTracker

    private lateinit var facebookAttendViewModel: FacebookAttendViewModel

    private val facebookId = "1"

    @Before
    fun setUp() {
        whenever(loginStateWatcher.loginStateSubject).thenReturn(BehaviorSubject.create())
        facebookAttendViewModel = FacebookAttendViewModel(loginStateWatcher, facebookRepository, firebaseAnalyticsEventTracker, facebookId)
    }

    @Test
    fun shouldNavigateToLoginScreenOnAttendClick() {
        whenever(loginStateWatcher.hasPermissions).thenReturn(false)
        val testObserver = facebookAttendViewModel.navigationRequests.test()

        facebookAttendViewModel.onAttendClick()

        testObserver.assertValue { it == NavigationRequest.LogIn }
    }

    @Test
    fun shouldSetAttendStatusToAttendingOnAttendClick() {
        whenever(facebookRepository.setEventAttending(facebookId)).thenReturn(Completable.complete())
        whenever(loginStateWatcher.hasPermissions).thenReturn(true)

        facebookAttendViewModel.onAttendClick()

        assertThat(facebookAttendViewModel.attendStatus.get(), equalTo(AttendStatus.ATTENDING))
    }

    @Test
    fun shouldOpenFacebookEventPageOnAttendClick() {
        whenever(loginStateWatcher.hasPermissions).thenReturn(true)
        facebookAttendViewModel.attendStatus.set(AttendStatus.UNSURE)
        val testObserver = facebookAttendViewModel.navigationRequests.test()

        facebookAttendViewModel.onAttendClick()

        testObserver.assertValue { it == NavigationRequest.Website("${Constants.Facebook.EVENT_URL}$facebookId") }
    }

    @Test
    fun shouldDoNothingOnAttendClickWhenIsPastEvent() {
        facebookAttendViewModel.isPastEvent.set(true)
        val testObserver = facebookAttendViewModel.navigationRequests.test()

        facebookAttendViewModel.onAttendClick()

        testObserver.assertNoValues()
        assertThat(facebookAttendViewModel.attendStatus.get(), equalTo(AttendStatus.DECLINED))
    }

}