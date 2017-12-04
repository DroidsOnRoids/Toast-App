package pl.droidsonrioids.toast.managers

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.api.EventDetailsResponse
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.Page
import pl.droidsonrioids.toast.data.model.SplitEvents

interface EventsRepository {

    fun getEvents(): Maybe<SplitEvents>

    fun getEvent(id: Long): Single<EventDetailsResponse>
    fun getEventsPage(page: Int): Single<Page<Event>>
}