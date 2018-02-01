package pl.droidsonroids.toast.app.facebook

import io.reactivex.subjects.BehaviorSubject
import pl.droidsonroids.toast.data.enums.LoginState

interface LoginStateWatcher {
    val loginStateSubject: BehaviorSubject<LoginState>
    val isLoggedIn: Boolean
}