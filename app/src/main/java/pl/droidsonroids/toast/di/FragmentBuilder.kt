package pl.droidsonroids.toast.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.droidsonroids.toast.app.contact.ContactFragment
import pl.droidsonroids.toast.app.events.EventsFragment
import pl.droidsonroids.toast.app.speakers.SpeakersFragment

@Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindSpeakersFragment(): SpeakersFragment

    @ContributesAndroidInjector
    abstract fun bindEventsFragment(): EventsFragment

    @ContributesAndroidInjector
    abstract fun bindContactFragment(): ContactFragment
}