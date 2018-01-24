package pl.droidsonroids.toast.data.api.speaker

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.api.ApiImage

data class ApiSpeaker(
        @SerializedName("id")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("job")
        val job: String,
        @SerializedName("avatar")
        val avatar: ApiImage
)