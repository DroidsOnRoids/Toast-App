package pl.droidsonroids.toast.di

import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class FacebookModule {

    @Singleton
    @Provides
    fun provideLoginManager() = LoginManager.getInstance()

    @Singleton
    @Provides
    fun provideCallbackManager() = CallbackManager.Factory.create()
}

typealias LoginCallbackManager = CallbackManager