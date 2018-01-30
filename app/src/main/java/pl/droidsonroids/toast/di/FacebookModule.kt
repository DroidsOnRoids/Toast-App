package pl.droidsonroids.toast.di

import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import dagger.Module
import dagger.Provides
import pl.droidsonroids.toast.app.login.FacebookLoginStateWatcher
import pl.droidsonroids.toast.app.login.LoginStateWatcher
import javax.inject.Singleton


@Module
class FacebookModule {

    @Singleton
    @Provides
    fun provideLoginManager(): LoginManager = LoginManager.getInstance()

    @Singleton
    @Provides
    fun provideCallbackManager(): CallbackManager = CallbackManager.Factory.create()

    @Singleton
    @Provides
    fun provideLoginStateWatcher(loginManager: LoginManager, loginCallbackManager: LoginCallbackManager): LoginStateWatcher = FacebookLoginStateWatcher(loginManager, loginCallbackManager)
}

typealias LoginCallbackManager = CallbackManager