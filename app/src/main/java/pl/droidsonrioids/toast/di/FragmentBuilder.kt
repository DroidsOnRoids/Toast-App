package pl.droidsonrioids.toast.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.droidsonrioids.toast.app.dummy.DummyFragment

@Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindDummyFragment(): DummyFragment
}