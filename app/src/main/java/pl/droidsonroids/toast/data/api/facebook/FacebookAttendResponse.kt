package pl.droidsonroids.toast.data.api.facebook

import com.google.gson.annotations.SerializedName

data class FacebookAttendResponse(
        @SerializedName("data")
        val data: List<ApiUserAttend>
)