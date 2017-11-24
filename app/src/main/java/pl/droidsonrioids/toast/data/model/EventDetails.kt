package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName

data class EventDetails(
        val id: Int,
        val title: String,
        val date: String,
        val facebook: Long,
        @SerializedName("place_name")
        val placeName: String,
        @SerializedName("place_street")
        val placeStreet: String,
        @SerializedName("place_coordinates")
        val placeCoordinates: Coordinates,
        @SerializedName("cover_images")
        val coverImages: List<CoverImage>
)