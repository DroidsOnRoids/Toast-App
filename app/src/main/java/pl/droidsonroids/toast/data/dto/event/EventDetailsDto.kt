package pl.droidsonroids.toast.data.dto.event

import pl.droidsonroids.toast.data.dto.ImageDto
import java.util.*

data class EventDetailsDto(
        val id: Long,
        val title: String,
        val date: Date,
        val facebookId: String,
        val placeName: String,
        val placeStreet: String,
        val coverImages: List<ImageDto>,
        val talks: List<EventTalkDto>,
        val photos: List<ImageDto>,
        val coordinates: CoordinatesDto
)