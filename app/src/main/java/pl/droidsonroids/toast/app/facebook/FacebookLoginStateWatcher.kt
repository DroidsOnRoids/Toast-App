package pl.droidsonroids.toast.app.facebook

import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonroids.toast.data.enums.LoginState
import pl.droidsonroids.toast.di.LoginCallbackManager
import pl.droidsonroids.toast.utils.Constants
import javax.inject.Inject


class FacebookLoginStateWatcher @Inject constructor(loginManager: LoginManager, loginCallbackManager: LoginCallbackManager) : LoginStateWatcher {
    override val loginStateSubject: BehaviorSubject<LoginState> = BehaviorSubject.createDefault(AccessToken.getCurrentAccessToken().loginState)

    override val isLoggedIn: Boolean get() = loginStateSubject.value == LoginState.LOGGED_IN

    override val hasPermissions: Boolean
        get() = AccessToken.getCurrentAccessToken().hasPermissions

    private val tokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
            loginStateSubject.onNext(currentAccessToken.loginState)
        }
    }

    init {
        tokenTracker.startTracking()
        loginManager.registerCallback(loginCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) = Unit

            override fun onCancel() = Unit

            override fun onError(error: FacebookException?) {
                loginStateSubject.onNext(LoginState.ERROR)
            }
        })
    }

    private val AccessToken?.loginState
        get() = if (this != null) LoginState.LOGGED_IN else LoginState.LOGGED_OUT

    private val AccessToken?.hasPermissions
        get() = this?.permissions?.containsAll(Constants.Facebook.PERMISSIONS)
                ?: false
}

