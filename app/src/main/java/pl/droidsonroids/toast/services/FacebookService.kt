package pl.droidsonroids.toast.services

import io.reactivex.Completable
import io.reactivex.Single
import pl.droidsonroids.toast.data.api.facebook.FacebookAttendResponse
import retrofit2.http.*

interface FacebookService {
    @GET("{eventId}/attending?field=rsvp_status")
    fun getEventAttendingState(@Header("Authorization") token: String, @Path("eventId") eventId: String, @Query("user") userId: String): Single<FacebookAttendResponse>

    @GET("{eventId}/interested?field=rsvp_status")
    fun getEventInterestedState(@Header("Authorization") token: String, @Path("eventId") eventId: String, @Query("user") userId: String): Single<FacebookAttendResponse>

    @POST("{eventId}/attending")
    fun postEventAttending(@Header("Authorization") token: String, @Path("eventId") eventId: String): Completable
}