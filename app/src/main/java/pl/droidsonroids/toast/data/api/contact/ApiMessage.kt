package pl.droidsonroids.toast.data.api.contact

import com.google.gson.annotations.SerializedName

data class ApiMessage(
        @SerializedName("email")
        val email: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("message")
        val message: String
)