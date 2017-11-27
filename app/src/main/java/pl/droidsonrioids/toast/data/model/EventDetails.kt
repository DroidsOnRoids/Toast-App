package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class EventDetails(
        val id: Int,
        val title: String,
        val date: Date,
        val facebook: String,
        @SerializedName("place_name")
        val placeName: String,
        @SerializedName("place_street")
        val placeStreet: String,
        @SerializedName("place_coordinates")
        val placeCoordinates: Coordinates,
        @SerializedName("cover_images")
        val coverImages: List<Image>,
        val photos: List<Image>
)