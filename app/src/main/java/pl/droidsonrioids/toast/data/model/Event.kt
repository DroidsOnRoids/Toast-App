package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName

data class Event(
        val id: Long,
        val title: String,
        val date: String,
        @SerializedName("cover_images")
        val coverImages: List<Image>
)