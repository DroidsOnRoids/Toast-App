package pl.droidsonroids.toast.data.api

import com.google.gson.annotations.SerializedName
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