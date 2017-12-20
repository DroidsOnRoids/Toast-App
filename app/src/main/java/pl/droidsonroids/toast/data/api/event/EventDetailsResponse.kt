package pl.droidsonroids.toast.data.api.event

import com.google.gson.annotations.SerializedName

data class EventDetailsResponse(
        @SerializedName("event")
        val eventItem: ApiEventDetails
)