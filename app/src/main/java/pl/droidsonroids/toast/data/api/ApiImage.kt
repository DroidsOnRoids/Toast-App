package pl.droidsonroids.toast.data.api

import com.google.gson.annotations.SerializedName

data class ApiImage(
        @SerializedName("big")
        val originalSizeUrl: String,
        @SerializedName("thumb")
        val thumbSizeUrl: String
)