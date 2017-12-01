package pl.droidsonrioids.toast.data.api

import com.google.gson.annotations.SerializedName
import pl.droidsonrioids.toast.data.model.Event

data class EventsResponse(
        val events: List<Event>,
        @SerializedName("page_count")
        val pageCount: Int
)