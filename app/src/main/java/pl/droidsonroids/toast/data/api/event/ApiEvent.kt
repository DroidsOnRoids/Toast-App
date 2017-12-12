package pl.droidsonroids.toast.data.api.event

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.api.ApiImage
import java.util.*

data class ApiEvent(
        @SerializedName("id")
        val id: Long,
        @SerializedName("title")
        val title: String,
        @SerializedName("date")
        val date: Date,
        @SerializedName("cover_images")
        val coverImages: List<ApiImage>
)