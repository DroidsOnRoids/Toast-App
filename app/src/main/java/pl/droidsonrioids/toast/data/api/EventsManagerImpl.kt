package pl.droidsonrioids.toast.data.api

import io.reactivex.Maybe
import pl.droidsonrioids.toast.data.model.Events
import javax.inject.Inject

class EventsManagerImpl @Inject constructor(private val eventService: EventService) : EventsManager {

    override fun getEvents(): Maybe<Events> =
            eventService.getEvents()
                    .flatMapMaybe {
                        it.events.firstOrNull()?.run {
                            getEvent(id).map { eventResponse ->
                                Events(eventResponse.eventItem, it.events.subList(1, it.events.size))
                            }.toMaybe()
                        }?:Maybe.empty<Events>()
                    }

    override fun getEvent(id: Long) =
            eventService.getEvent(id)

}