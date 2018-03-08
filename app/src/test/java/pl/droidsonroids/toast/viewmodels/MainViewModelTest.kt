package pl.droidsonroids.toast.viewmodels

import android.databinding.ObservableField
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.utils.NavigationRequest

class MainViewModelTest : RxTestBase() {
    @Mock
    lateinit var loginStateWatcher: LoginStateWatcher

    lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        whenever(loginStateWatcher.loginStateSubject).thenReturn(BehaviorSubject.create())
        mainViewModel = MainViewModel(loginStateWatcher, ObservableField(0f))
    }

    @Test
    fun shouldRequestNavigationToLogIn() {
        val testObserver = mainViewModel.navigationSubject.test()
        mainViewModel.onLogInClick()

        testObserver.assertValue { it == NavigationRequest.LogIn }
    }

    @Test
    fun shouldRequestNavigationToLogOut() {
        val testObserver = mainViewModel.navigationSubject.test()
        mainViewModel.onLogOutClick()

        testObserver.assertValue { it == NavigationRequest.LogOut }
    }

}