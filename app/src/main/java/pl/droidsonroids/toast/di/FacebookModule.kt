package pl.droidsonroids.toast.di

import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonroids.toast.app.facebook.FacebookUserManager
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.app.facebook.LoginStateWatcherPlaceholder
import pl.droidsonroids.toast.app.facebook.UserManager
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.data.enums.AttendStatusAdapter
import pl.droidsonroids.toast.repositories.facebook.FacebookRepository
import pl.droidsonroids.toast.repositories.facebook.FacebookRepositoryImpl
import pl.droidsonroids.toast.services.FacebookService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


private const val BASE_FACEBOOK_URL = "https://graph.facebook.com/v2.11/"

@Module
class FacebookModule {

    @Singleton
    @Provides
    fun provideLoginManager(): LoginManager = LoginManager.getInstance()

    @Singleton
    @Provides
    fun provideCallbackManager(): CallbackManager = CallbackManager.Factory.create()

    //    Replace with FacebookLoginStateWatcher to use Facebook SDK
    @Singleton
    @Provides
    fun provideLoginStateWatcher(): LoginStateWatcher = LoginStateWatcherPlaceholder()

    @Singleton
    @Provides
    fun provideUserManager(): UserManager = FacebookUserManager()

    @Singleton
    @Provides
    fun provideFacebookRepository(facebookService: FacebookService, userManager: UserManager): FacebookRepository = FacebookRepositoryImpl(facebookService, userManager)

    @Singleton
    @Provides
    fun provideFacebookService(httpClient: OkHttpClient): FacebookService {
        val gson = GsonBuilder().registerTypeAdapter(AttendStatus::class.java, AttendStatusAdapter()).create()
        return Retrofit.Builder()
                .baseUrl(BASE_FACEBOOK_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient)
                .build()
                .create(FacebookService::class.java)
    }
}

typealias LoginCallbackManager = CallbackManager