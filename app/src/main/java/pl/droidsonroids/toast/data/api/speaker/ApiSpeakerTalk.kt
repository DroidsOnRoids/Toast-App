package pl.droidsonroids.toast.data.api.speaker

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.api.event.ApiEvent

data class ApiSpeakerTalk(
        @SerializedName("id")
        val id: Long,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("event")
        val event: ApiEvent
)