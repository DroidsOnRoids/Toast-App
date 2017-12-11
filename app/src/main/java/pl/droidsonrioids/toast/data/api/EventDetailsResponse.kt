package pl.droidsonrioids.toast.data.api

import com.google.gson.annotations.SerializedName

data class EventDetailsResponse(
        @SerializedName("event")
        val eventItem: ApiEventDetails
)