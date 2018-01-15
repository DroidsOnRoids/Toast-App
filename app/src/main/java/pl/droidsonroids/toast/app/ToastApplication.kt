package pl.droidsonroids.toast.app

import android.app.Activity
import android.app.Application
import android.content.res.Resources
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.droidsonroids.toast.di.DaggerAppComponent
import javax.inject.Inject

class ToastApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityInjector


    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
        appResources = resources
    }

    companion object {
        lateinit var appResources: Resources
    }
}