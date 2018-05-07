package pl.droidsonroids.toast.app

import android.app.Activity
import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import pl.droidsonroids.toast.BuildConfig
import pl.droidsonroids.toast.app.notifications.FcmSubscriptionManager
import pl.droidsonroids.toast.di.DaggerAppComponent
import pl.droidsonroids.toast.utils.BASE_URL_KEY
import pl.droidsonroids.toast.utils.IMAGE_URL_KEY
import timber.log.Timber
import javax.inject.Inject

private const val CRASHLYTICS_KEY_PRIORITY = "priority"
private const val CRASHLYTICS_KEY_TAG = "tag"
private const val CRASHLYTICS_KEY_MESSAGE = "message"

class ToastApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fcmSubscriptionManager: FcmSubscriptionManager

    override fun activityInjector() = activityInjector

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupTimber()
        setupCrashlytics()
        setupRemoteConfig()
        setupNotificationManager()
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

    private fun setupRemoteConfig() {
        FirebaseRemoteConfig.getInstance().apply {
            FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build()
                    .let(::setConfigSettings)
            mapOf(
                    BASE_URL_KEY to BuildConfig.BASE_API_URL,
                    IMAGE_URL_KEY to BuildConfig.BASE_IMAGES_URL
            ).let(::setDefaults)
        }
    }

    private fun setupNotificationManager() {
        fcmSubscriptionManager.init()
    }

    private class CrashlyticsTree : Timber.Tree() {

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