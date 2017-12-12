package pl.droidsonroids.toast.services

import io.reactivex.Single
import pl.droidsonroids.toast.data.api.speaker.SpeakersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpeakerService {
    @GET("speakers")
    fun getSpeakers(@Query("per_page") pageSize: Int = 10, @Query("page") pageNumber: Int = 1): Single<SpeakersResponse>
}
