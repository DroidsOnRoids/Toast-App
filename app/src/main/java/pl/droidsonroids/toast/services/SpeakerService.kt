package pl.droidsonroids.toast.services

import io.reactivex.Single
import pl.droidsonroids.toast.data.api.speaker.SpeakerDetailsResponse
import pl.droidsonroids.toast.data.api.speaker.SpeakersResponse
import pl.droidsonroids.toast.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpeakerService {
    @GET("speakers")
    fun getSpeakers(
            @Query("order") sortingType: String,
            @Query("per_page") pageSize: Int = Constants.Page.SIZE,
            @Query("page") pageNumber: Int = Constants.Page.FIRST
    ): Single<SpeakersResponse>

    @GET("speakers")
    fun searchSpeakers(@Query("query") query: String, @Query("per_page") pageSize: Int = Constants.Page.SIZE, @Query("page") pageNumber: Int = Constants.Page.FIRST): Single<SpeakersResponse>

    @GET("speakers/{id}")
    fun getSpeaker(@Path("id") id: Long): Single<SpeakerDetailsResponse>
}
