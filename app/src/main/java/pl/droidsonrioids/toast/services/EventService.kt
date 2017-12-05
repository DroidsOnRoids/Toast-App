package pl.droidsonrioids.toast.services

import io.reactivex.Single
import pl.droidsonrioids.toast.data.api.EventDetailsResponse
import pl.droidsonrioids.toast.data.api.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface EventService {

    @GET("events")
    fun getEvents(): Single<EventsResponse>

    @GET("events/{id}")
    fun getEvent(@Path("id") id: Long): Single<EventDetailsResponse>

}