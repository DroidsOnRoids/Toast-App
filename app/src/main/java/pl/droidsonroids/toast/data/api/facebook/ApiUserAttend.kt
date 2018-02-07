package pl.droidsonroids.toast.data.api.facebook

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.toast.data.enums.AttendStatus

data class ApiUserAttend(
        @SerializedName("rsvp_status")
        val attendStatus: AttendStatus
)