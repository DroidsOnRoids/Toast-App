package pl.droidsonroids.toast.data.dto.event

import pl.droidsonroids.toast.data.dto.ImageDto
import java.util.*

data class EventDto(
        val id: Long,
        val title: String,
        val date: Date,
        val coverImages: List<ImageDto>
)