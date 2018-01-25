package pl.droidsonroids.toast.data.api.speaker

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.api.ApiImage


data class ApiSpeakerDetails(
        @SerializedName("id")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("job")
        val job: String,
        @SerializedName("avatar")
        val avatar: ApiImage,
        @SerializedName("bio")
        val bio: String,
        @SerializedName("talks")
        val talks: List<ApiSpeakerTalk>
)