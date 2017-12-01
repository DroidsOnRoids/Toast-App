package pl.droidsonrioids.toast.managers

import io.reactivex.Maybe
import pl.droidsonrioids.toast.data.model.SplitEvents
import pl.droidsonrioids.toast.services.EventService
import javax.inject.Inject

class EventsManagerImpl @Inject constructor(private val eventService: EventService) : EventsManager {

    override fun getEvents(): Maybe<SplitEvents> =
            eventService.getEvents()
                    .flatMapMaybe {
                        it.events.firstOrNull()?.run {
                            getEvent(id).map { eventResponse ->
                                SplitEvents(eventResponse.eventItem, it.events.subList(1, it.events.size))
                            }.toMaybe()
                        }?:Maybe.empty<SplitEvents>()
                    }

    override fun getEvent(id: Long) =
            eventService.getEvent(id)

}