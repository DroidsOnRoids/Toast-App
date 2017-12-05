package pl.droidsonrioids.toast.managers

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.api.ApiEvent
import pl.droidsonrioids.toast.data.dto.EventDetailsDto
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.data.dto.SplitEvents
import pl.droidsonrioids.toast.data.mapper.toDto
import pl.droidsonrioids.toast.services.EventService
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(private val eventService: EventService) : EventsRepository {

    override fun getEvents(): Maybe<SplitEvents> {
        return eventService.getEvents()
                .flatMapMaybe { (events) ->
                    mapToSplitEventsMaybe(events)
                }
    }

    private fun mapToSplitEventsMaybe(events: List<ApiEvent>): Maybe<SplitEvents> {
        val eventsDto = events.map { it.toDto() }
        return eventsDto
                .firstOrNull()
                ?.getSplitEventsMaybe(eventsDto)
                ?: Maybe.empty<SplitEvents>()
    }

    private fun EventDto.getSplitEventsMaybe(previousEvents: List<EventDto>): Maybe<SplitEvents> {
        return getEvent(id)
                .map { eventDetails ->
                    SplitEvents(eventDetails, previousEvents.subList(1, previousEvents.size))
                }.toMaybe()
    }

    override fun getEvent(id: Long): Single<EventDetailsDto> =
            eventService.getEvent(id).map {
                it.eventItem.toDto()
            }

}