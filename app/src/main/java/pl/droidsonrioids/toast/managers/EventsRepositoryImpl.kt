package pl.droidsonrioids.toast.managers

import io.reactivex.Maybe
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.SplitEvents
import pl.droidsonrioids.toast.services.EventService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(private val eventService: EventService) : EventsRepository {

    override fun getEvents(): Maybe<SplitEvents> {
        return eventService.getEvents()
                .flatMapMaybe { eventsResponse ->
                    eventsResponse.events
                            .firstOrNull()
                            ?.getSplitEventsMaybe(eventsResponse.events)
                            ?: Maybe.empty<SplitEvents>()
                }.delay(2, TimeUnit.SECONDS)
    }

    private fun Event.getSplitEventsMaybe(previousEvents: List<Event>): Maybe<SplitEvents> {
        return getEvent(id)
                .map { eventDetailsResponse ->
                    SplitEvents(eventDetailsResponse.eventItem, previousEvents.subList(1, previousEvents.size))
                }.toMaybe()
    }

    override fun getEvent(id: Long) =
            eventService.getEvent(id)

}