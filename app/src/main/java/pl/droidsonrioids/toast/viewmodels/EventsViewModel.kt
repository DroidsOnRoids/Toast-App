package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonrioids.toast.data.api.EventsManager
import pl.droidsonrioids.toast.data.model.Event
import javax.inject.Inject


class EventsViewModel @Inject constructor(eventsManager: EventsManager) : ViewModel() {


    var featuredEvent: ObservableField<UpcomingEventViewModel> = ObservableField()
    var lastEvents: List<Event> = listOf()
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