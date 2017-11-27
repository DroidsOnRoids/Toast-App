package pl.droidsonrioids.toast.data.model

import com.google.gson.annotations.SerializedName

data class EventDetailsResponse(
        @SerializedName("event")
        val eventItem: EventDetailsImpl
)