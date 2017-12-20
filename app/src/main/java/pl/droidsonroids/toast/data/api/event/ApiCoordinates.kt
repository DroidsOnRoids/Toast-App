package pl.droidsonroids.toast.data.api.event

import com.google.gson.annotations.SerializedName

data class ApiCoordinates(
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("longitude")
        val longitude: Double
)