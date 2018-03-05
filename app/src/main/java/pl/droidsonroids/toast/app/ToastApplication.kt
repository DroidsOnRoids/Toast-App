package pl.droidsonroids.toast.app

import android.app.Activity
import android.app.Application
import android.util.Log
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.droidsonroids.toast.BuildConfig
import pl.droidsonroids.toast.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

private const val CRASHLYTICS_KEY_PRIORITY = "priority"
private const val CRASHLYTICS_KEY_TAG = "tag"
private const val CRASHLYTICS_KEY_MESSAGE = "message"

class ToastApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityInjector

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupTimber()
        setupCrashlytics()
    }

    private fun setupDagger() {
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    private fun setupTimber() {
        Timber.plant(
                if (BuildConfig.DEBUG) {
                    Timber.DebugTree()
                } else {
                    CrashlyticsTree()
                }
        )
    }

    private fun setupCrashlytics() {
        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()

        val crashlyticsKit = Crashlytics.Builder()
                .core(crashlyticsCore)
                .build()

        val fabric = Fabric.Builder(this)
                .kits(crashlyticsKit)
                .debuggable(true)
                .build()

        Fabric.with(fabric)
    }

    private inner class CrashlyticsTree : Timber.Tree() {

        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
            if (priority == Log.ERROR) {
                Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority)
                Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag)
                Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message)
                Crashlytics.logException(throwable)
            }
        }
    }
}