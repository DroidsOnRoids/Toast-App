package pl.droidsonrioids.toast.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.droidsonrioids.toast.app.speakers.SpeakersFragment

@Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindSpeakersFragment(): SpeakersFragment
}