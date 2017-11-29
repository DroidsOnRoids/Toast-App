package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Event(
        val id: Long,
        val title: String,
        val date: Date,
        @SerializedName("cover_images")
        val coverImages: List<Image>
)