package pl.droidsonrioids.toast.repositories

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import pl.droidsonrioids.toast.data.Page
import pl.droidsonrioids.toast.data.api.ApiEvent
import pl.droidsonrioids.toast.data.dto.EventDetailsDto
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.data.dto.SplitEvents
import pl.droidsonrioids.toast.data.mapper.toDto
import pl.droidsonrioids.toast.services.EventService
import pl.droidsonrioids.toast.utils.toPage
import javax.inject.Inject

const val FIRST_PAGE = 1

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
                    SplitEvents(featuredEvent, Page(previousEvents, FIRST_PAGE, pageCount))
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