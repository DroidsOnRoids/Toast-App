package pl.droidsonrioids.toast.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonrioids.toast.BuildConfig
import pl.droidsonrioids.toast.data.api.EventService
import pl.droidsonrioids.toast.data.api.EventsManager
import pl.droidsonrioids.toast.data.api.EventsManagerImpl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val ACCEPT = "Accept"
private const val APPLICATION_JSON = "application/json"

@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideApiManager(eventService: EventService): EventsManager = EventsManagerImpl(eventService)

    @Singleton
    @Provides
    fun provideApiService(httpClient: OkHttpClient): EventService =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(httpClient)
                    .build()
                    .create(EventService::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor {
                        it.proceed(it.request().newBuilder().header(ACCEPT, APPLICATION_JSON).build())
                    }
                    .build()
}