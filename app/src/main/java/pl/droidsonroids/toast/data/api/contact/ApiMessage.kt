package pl.droidsonroids.toast.data.api.contact

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.MessageType

data class ApiMessage(
        @SerializedName("email")
        val email: String,
        @SerializedName("type")
        val type: MessageType,
        @SerializedName("name")
        val name: String,
        @SerializedName("message")
        val message: String
)