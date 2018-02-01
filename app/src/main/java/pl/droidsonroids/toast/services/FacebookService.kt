package pl.droidsonroids.toast.services

import io.reactivex.Single
import pl.droidsonroids.toast.data.api.facebook.FacebookAttendResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FacebookService {
    @GET("{eventId}/attending?field=rsvp_status")
    fun getEventAttendingState(@Header("Authorization") token: String, @Path("eventId") eventId: String, @Query("userId") userId: String): Single<FacebookAttendResponse>

    @GET("{eventId}/interested?field=rsvp_status")
    fun getEventInterestedState(@Header("Authorization") token: String, @Path("eventId") eventId: String, @Query("userId") userId: String): Single<FacebookAttendResponse>
}