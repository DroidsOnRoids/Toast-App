package pl.droidsonrioids.toast.managers

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.Page
import pl.droidsonrioids.toast.data.model.SplitEvents
import pl.droidsonrioids.toast.services.EventService
import javax.inject.Inject

class EventsManagerImpl @Inject constructor(private val eventService: EventService) : EventsManager {

    override fun getEvents(): Maybe<SplitEvents> =
            eventService.getEvents()
                    .flatMapMaybe {
                        it.events.firstOrNull()?.run {
                            getEvent(id).map { eventResponse ->
                                SplitEvents(eventResponse.eventItem, Page(it.events.subList(1, it.events.size), 1, it.pageCount))
                            }.toMaybe()
                        } ?: Maybe.empty<SplitEvents>()
                    }

    override fun getEvent(id: Long) =
            eventService.getEvent(id)

    override fun getEventsPage(page: Int): Single<Page<Event>> =
            eventService.getEvents(page = page)
                    .map { Page(it.events, page, it.pageCount) }
}