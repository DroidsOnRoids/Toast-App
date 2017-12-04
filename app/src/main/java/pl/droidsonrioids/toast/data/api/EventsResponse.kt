package pl.droidsonrioids.toast.data.api

import com.google.gson.annotations.SerializedName
import pl.droidsonrioids.toast.data.model.EventDto

data class EventsResponse(
        @SerializedName("events")
        val events: List<EventDto>,
        @SerializedName("page_count")
        val pageCount: Int
)