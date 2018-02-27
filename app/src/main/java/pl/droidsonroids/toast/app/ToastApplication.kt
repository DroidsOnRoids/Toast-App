package pl.droidsonroids.toast.app

import android.app.Activity
import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
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
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

        Timber.plant(
                if (BuildConfig.DEBUG) {
                    Timber.DebugTree()
                } else {
                    CrashlyticsTree()
                }
        )

        Fabric.with(this, Crashlytics())
    }

    private inner class CrashlyticsTree : Timber.Tree() {

        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
            if (priority == Log.ERROR) {
                val exception = throwable ?: Exception(message)
                Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority)
                Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag)
                Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message)
                Crashlytics.logException(exception)
            }
        }
    }
}