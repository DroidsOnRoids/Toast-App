package pl.droidsonroids.toast.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.droidsonroids.toast.app.home.MainActivity

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}