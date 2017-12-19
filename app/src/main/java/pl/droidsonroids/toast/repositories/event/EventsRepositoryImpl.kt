package pl.droidsonroids.toast.repositories.event

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.api.event.ApiEvent
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.data.dto.event.EventDto
import pl.droidsonroids.toast.data.dto.event.SplitEvents
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.services.EventService
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.toPage
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(private val eventService: EventService) : EventsRepository {

    override fun getEvents(): Maybe<SplitEvents> {
        return eventService.getEvents()
                .flatMapMaybe { (events, pageCount) ->
                    mapToSplitEventsMaybe(events, pageCount)
                }
    }

    private fun mapToSplitEventsMaybe(events: List<ApiEvent>, pageCount: Int): Maybe<SplitEvents> {
        val eventsDto = events.map { it.toDto() }
        return eventsDto
                .firstOrNull()
                ?.getSplitEventsMaybe(eventsDto.slice(1 until eventsDto.size), pageCount)
                ?: Maybe.empty<SplitEvents>()
    }

    private fun EventDto.getSplitEventsMaybe(previousEvents: List<EventDto>, pageCount: Int): Maybe<SplitEvents> {
        return getEvent(id)
                .map { featuredEvent ->
                    SplitEvents(featuredEvent, Page(previousEvents, Constants.FIRST_PAGE, pageCount))
                }
                .toMaybe()
    }

    override fun getEvent(id: Long): Single<EventDetailsDto> =
            eventService.getEvent(id).map { it.eventItem.toDto() }

    override fun getEventsPage(pageNumber: Int): Single<Page<EventDto>> {
        return eventService.getEvents(pageNumber = pageNumber)
                .flatMap { (apiEvents, pageCount) ->
                    apiEvents.toObservable()
                            .map { it.toDto() }
                            .toPage(pageNumber, pageCount)
                }
    }
}