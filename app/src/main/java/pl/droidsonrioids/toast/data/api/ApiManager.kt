package pl.droidsonrioids.toast.data.api

import io.reactivex.Single
import pl.droidsonrioids.toast.data.model.EventDetailsResponse
import pl.droidsonrioids.toast.data.model.EventsResponse

interface ApiManager {
    fun getEvents(): Single<EventsResponse>
    fun getEvent(id: Int): Single<EventDetailsResponse>
}