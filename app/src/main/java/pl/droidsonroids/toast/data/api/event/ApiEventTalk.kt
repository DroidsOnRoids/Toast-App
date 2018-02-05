package pl.droidsonroids.toast.data.api.event

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker

data class ApiEventTalk(
        @SerializedName("id")
        val id: Long,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("speaker")
        val speaker: ApiSpeaker
)