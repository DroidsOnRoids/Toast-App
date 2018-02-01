package pl.droidsonroids.toast.viewmodels

import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.login.LoginStateWatcher
import pl.droidsonroids.toast.utils.NavigationRequest

class MainViewModelTest : RxTestBase() {
    @Mock
    lateinit var loginStateWatcher: LoginStateWatcher

    @InjectMocks
    lateinit var mainViewModel: MainViewModel

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