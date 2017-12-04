package pl.droidsonrioids.toast.repositories

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.Page
import pl.droidsonrioids.toast.data.model.SplitEvents
import pl.droidsonrioids.toast.services.EventService
import javax.inject.Inject

const val FIRST_PAGE = 1

class EventsRepositoryImpl @Inject constructor(private val eventService: EventService) : EventsRepository {

    override fun getEvents(): Maybe<SplitEvents> {
        return eventService.getEvents()
                .flatMapMaybe { (events, pageCount) ->
                    events.firstOrNull()
                            ?.getSplitEventsMaybe(events, pageCount)
                            ?: Maybe.empty<SplitEvents>()
                }
    }

    private fun Event.getSplitEventsMaybe(previousEvents: List<Event>, pageCount: Int): Maybe<SplitEvents> {
        return getEvent(id).map { (eventItem) ->
            SplitEvents(eventItem, Page(previousEvents.subList(1, previousEvents.size), FIRST_PAGE, pageCount))
        }.toMaybe()
    }

    override fun getEvent(id: Long) =
            eventService.getEvent(id)

    override fun getEventsPage(page: Int): Single<Page<Event>> =
            eventService.getEvents(page = page)
                    .map { (events, pageCount) -> Page(events, page, pageCount) }
}