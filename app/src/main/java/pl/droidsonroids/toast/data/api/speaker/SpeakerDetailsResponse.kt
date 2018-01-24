package pl.droidsonroids.toast.data.api.speaker

import com.google.gson.annotations.SerializedName

data class SpeakerDetailsResponse(
        @SerializedName("speaker")
        val speakerItem: ApiSpeaker
)