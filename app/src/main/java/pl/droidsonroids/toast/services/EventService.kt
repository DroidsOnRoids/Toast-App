package pl.droidsonroids.toast.services

import io.reactivex.Single
import pl.droidsonroids.toast.data.api.event.EventDetailsResponse
import pl.droidsonroids.toast.data.api.event.EventsResponse
import pl.droidsonroids.toast.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface EventService {

    @GET("events")
    fun getEvents(@Query("per_page") pageSize: Int = Constants.PAGE_SIZE, @Query("page") pageNumber: Int = Constants.FIRST_PAGE): Single<EventsResponse>

    @GET("events/{id}")
    fun getEvent(@Path("id") id: Long): Single<EventDetailsResponse>

}