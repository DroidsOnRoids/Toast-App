package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class EventDetailsImpl(
        override val id: Int,
        override val title: String,
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
        override val photos: List<Image>
) : EventDetails