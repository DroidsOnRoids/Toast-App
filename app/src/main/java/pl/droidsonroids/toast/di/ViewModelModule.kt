package pl.droidsonroids.toast.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.droidsonroids.toast.viewmodels.MainViewModel
import pl.droidsonroids.toast.viewmodels.event.EventDetailsViewModel
import pl.droidsonroids.toast.viewmodels.event.EventsViewModel
import pl.droidsonroids.toast.viewmodels.speaker.SpeakersSearchViewModel
import pl.droidsonroids.toast.viewmodels.speaker.SpeakersViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    abstract fun bindEventsViewModel(eventsViewModel: EventsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpeakersViewModel::class)
    abstract fun bindSpeakersViewModel(speakersViewModel: SpeakersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpeakersSearchViewModel::class)
    abstract fun bindSpeakersListViewModel(speakersSearchViewModel: SpeakersSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventDetailsViewModel::class)
    abstract fun bindEventDetailsViewModel(eventDetailsViewModel: EventDetailsViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}