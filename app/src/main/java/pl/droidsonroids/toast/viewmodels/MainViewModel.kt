package pl.droidsonroids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.app.login.FacebookLoginStateWatcher
import pl.droidsonroids.toast.app.login.LoginStateWatcher
import pl.droidsonroids.toast.utils.NavigationRequest
import javax.inject.Inject

class MainViewModel @Inject constructor(facebookLoginStateWatcher: FacebookLoginStateWatcher) : ViewModel(), NavigatingViewModel, LoginStateWatcher by facebookLoginStateWatcher {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    fun onSpeakerSearchRequested() {
        navigationSubject.onNext(NavigationRequest.SpeakersSearch)
    }

    fun onLogInClick() {
        navigationSubject.onNext(NavigationRequest.LogIn)
    }

    fun onLogOutClick() {
        navigationSubject.onNext(NavigationRequest.LogOut)
    }
}