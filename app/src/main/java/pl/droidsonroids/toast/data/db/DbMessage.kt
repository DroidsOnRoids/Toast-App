package pl.droidsonroids.toast.data.db

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.enums.MessageType

data class DbMessage(
        @SerializedName("email")
        val email: String? = null,
        @SerializedName("type")
        val type: MessageType? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("message")
        val message: String? = null
)