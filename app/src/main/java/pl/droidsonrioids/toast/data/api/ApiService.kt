package pl.droidsonrioids.toast.data.api

import io.reactivex.Single
import pl.droidsonrioids.toast.data.model.EventDetailsResponse
import pl.droidsonrioids.toast.data.model.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("events")
    fun getEvents(): Single<EventsResponse>

    @GET("events/{id}")
    fun getEvent(@Path("id") id: Int): Single<EventDetailsResponse>

}