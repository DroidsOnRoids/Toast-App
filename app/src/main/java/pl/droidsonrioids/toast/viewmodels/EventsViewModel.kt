package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.managers.EventsManager
import javax.inject.Inject


class EventsViewModel @Inject constructor(eventsManager: EventsManager) : ViewModel() {


    var featuredEvent: ObservableField<UpcomingEventViewModel> = ObservableField()
        private set
    // TODO:  TOA-42 Add previous events handling
    var lastEvents: List<Event> = listOf()
        private set
    private val disposable: Disposable

    init {
        disposable = eventsManager.getEvents().subscribeBy(onSuccess = {
            featuredEvent.set(UpcomingEventViewModel(it.upcomingEvent))
            lastEvents = it.lastEvents
        })
    }

    override fun onCleared() {
        disposable.dispose()
    }

}