package pl.droidsonrioids.toast.data.api

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.model.EventDetailsResponse
import pl.droidsonrioids.toast.data.model.Events

interface EventsManager {

    fun getEvents(): Maybe<Events>

    fun getEvent(id: Long): Single<EventDetailsResponse>
}