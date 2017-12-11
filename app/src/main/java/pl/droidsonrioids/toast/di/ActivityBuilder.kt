package pl.droidsonrioids.toast.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.droidsonrioids.toast.app.home.MainActivity

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}