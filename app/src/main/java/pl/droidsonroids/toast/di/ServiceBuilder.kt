package pl.droidsonroids.toast.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.droidsonroids.toast.app.notifications.LocalNotificationService

@Module
abstract class ServiceBuilder {
    @ContributesAndroidInjector
    abstract fun bindNotificationService(): LocalNotificationService
}