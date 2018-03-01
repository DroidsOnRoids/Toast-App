package pl.droidsonroids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.data.enums.LoginState
import pl.droidsonroids.toast.utils.NavigationRequest
import javax.inject.Inject

class MainViewModel @Inject constructor(loginStateWatcher: LoginStateWatcher) : ViewModel(), NavigatingViewModel, LoginStateWatcher by loginStateWatcher {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    private var loginStateDisposable = Disposables.disposed()

    init {
        loginStateDisposable = loginStateSubject
                .filter { it != LoginState.ERROR }
                .subscribe {
                    val message = when (it) {
                        LoginState.LOGGED_IN -> R.string.logged_in_successfully
                        else -> R.string.logged_out_successfully
                    }
                    navigationSubject.onNext(NavigationRequest.SnackBar(message))
                }
    }

    fun onSpeakerSearchRequested() {
        navigationSubject.onNext(NavigationRequest.SpeakersSearch)
    }

    fun onLogInClick() {
        navigationSubject.onNext(NavigationRequest.LogIn)
    }

    fun onLogOutClick() {
        navigationSubject.onNext(NavigationRequest.LogOut)
    }

    override fun onCleared() {
        loginStateDisposable.dispose()
    }
}