package pl.droidsonroids.toast.app.facebook

import io.reactivex.subjects.BehaviorSubject
import pl.droidsonroids.toast.data.enums.LoginState

class LoginStateWatcherPlaceholder : LoginStateWatcher {
    override val loginStateSubject: BehaviorSubject<LoginState> = BehaviorSubject.createDefault(LoginState.LOGGED_OUT)
    override val isLoggedIn = false
    override val hasPermissions = false
}