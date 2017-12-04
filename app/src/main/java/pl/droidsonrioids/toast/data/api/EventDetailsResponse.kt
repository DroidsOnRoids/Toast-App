package pl.droidsonrioids.toast.data.api

import com.google.gson.annotations.SerializedName
import pl.droidsonrioids.toast.data.model.EventDetailsDto

data class EventDetailsResponse(
        @SerializedName("event")
        val eventItem: EventDetailsDto
)