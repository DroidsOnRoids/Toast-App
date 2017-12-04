package pl.droidsonrioids.toast.data.api

import com.google.gson.annotations.SerializedName
import pl.droidsonrioids.toast.data.model.Event

data class EventsResponse(
        @SerializedName("events")
        val events: List<Event>
)