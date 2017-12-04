package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class EventDto(
        @SerializedName("id")
        override val id: Long,
        @SerializedName("title")
        override val title: String,
        @SerializedName("date")
        override val date: Date,
        @SerializedName("cover_images")
        override val coverImages: List<Image>
) : Event