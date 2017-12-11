package pl.droidsonroids.toast.data.dto

import java.util.*

data class EventDetailsDto(
        val id: Int,
        val title: String,
        val date: Date,
        val facebookId: String,
        val placeName: String,
        val placeStreet: String,
        val coverImages: List<ImageDto>
)