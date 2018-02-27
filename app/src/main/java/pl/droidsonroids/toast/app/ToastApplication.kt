package pl.droidsonroids.toast.app

import android.app.Activity
import android.app.Application
import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.droidsonroids.toast.BuildConfig
import pl.droidsonroids.toast.di.DaggerAppComponent
import timber.log.Timber
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

        Timber.plant(
                if (BuildConfig.DEBUG) {
                    Timber.DebugTree()
                } else {
                    CrashReportingTree()
                }
        )
    }

    private inner class CrashReportingTree : Timber.Tree() {

        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
            if (priority != Log.VERBOSE && priority != Log.DEBUG) {
                val exception = throwable ?: Exception(message)
                FirebaseCrash.logcat(priority, tag, message)
                FirebaseCrash.report(exception)
            }
        }
    }
}