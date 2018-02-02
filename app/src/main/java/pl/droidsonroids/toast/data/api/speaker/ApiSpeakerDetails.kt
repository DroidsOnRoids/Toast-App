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
        @SerializedName("bio")
        val bio: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("github_url")
        val github: String,
        @SerializedName("website_url")
        val website: String,
        @SerializedName("twitter_url")
        val twitter: String,
        @SerializedName("avatar")
        val avatar: ApiImage,
        @SerializedName("talks")
        val talks: List<ApiSpeakerTalk>
)