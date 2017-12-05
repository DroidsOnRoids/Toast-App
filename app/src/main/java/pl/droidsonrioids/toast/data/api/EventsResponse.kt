package pl.droidsonrioids.toast.data.api

import com.google.gson.annotations.SerializedName

data class EventsResponse(
        @SerializedName("events")
        val events: List<ApiEvent>
)