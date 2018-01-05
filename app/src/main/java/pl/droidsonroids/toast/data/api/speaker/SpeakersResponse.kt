package pl.droidsonroids.toast.data.api.speaker

import com.google.gson.annotations.SerializedName

data class SpeakersResponse(
        @SerializedName("speakers")
        val speakers: List<ApiSpeaker>,
        @SerializedName("page_count")
        val allPagesCount: Int
)