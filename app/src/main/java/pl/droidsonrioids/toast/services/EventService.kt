package pl.droidsonrioids.toast.services

import io.reactivex.Single
import pl.droidsonrioids.toast.data.api.EventDetailsResponse
import pl.droidsonrioids.toast.data.api.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface EventService {

    @GET("events")
    fun getEvents(@Query("per_page") pageSize: Int = 10, @Query("page") pageNumber: Int = 1): Single<EventsResponse>

    @GET("events/{id}")
    fun getEvent(@Path("id") id: Long): Single<EventDetailsResponse>

}