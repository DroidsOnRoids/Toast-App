package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class EventDetailsDto(
        @SerializedName("id")
        override val id: Int,
        @SerializedName("title")
        override val title: String,
        @SerializedName("date")
        override val date: Date,
        @SerializedName("facebook")
        override val facebookId: String,
        @SerializedName("place_name")
        override val placeName: String,
        @SerializedName("place_street")
        override val placeStreet: String,
        @SerializedName("place_coordinates")
        override val placeCoordinates: Coordinates,
        @SerializedName("cover_images")
        override val coverImages: List<Image>,
        @SerializedName("photos")
        override val photos: List<Image>
) : EventDetails