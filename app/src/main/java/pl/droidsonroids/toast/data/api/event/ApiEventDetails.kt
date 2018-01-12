package pl.droidsonroids.toast.data.api.event

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.api.ApiImage
import java.util.*

data class ApiEventDetails(
        @SerializedName("id")
        val id: Long,
        @SerializedName("title")
        val title: String,
        @SerializedName("date")
        val date: Date,
        @SerializedName("facebook")
        val facebookId: String,
        @SerializedName("place_name")
        val placeName: String,
        @SerializedName("place_street")
        val placeStreet: String,
        @SerializedName("place_coordinates")
        val placeCoordinates: ApiCoordinates,
        @SerializedName("cover_images")
        val coverImages: List<ApiImage>,
        @SerializedName("photos")
        val photos: List<ApiImage>,
        @SerializedName("talks")
        val talks: List<ApiTalk>
)