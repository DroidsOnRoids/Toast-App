package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonrioids.toast.data.api.EventsManager
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.EventDetails
import javax.inject.Inject


class EventsViewModel @Inject constructor(private val eventsManager: EventsManager) : ViewModel() {

    var featuredEvent: EventDetails? = null
    var lastEvents: List<Event> = listOf()

    init {
        eventsManager.getEvents().subscribeBy (onSuccess = {
            featuredEvent = it.featuredEvent
            lastEvents = it.lastEvents
        })
    }

    fun onViewCreated() {
        getUpcomingEventData()
    }

    private fun getUpcomingEventData() {

    }
}