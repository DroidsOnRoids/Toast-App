package pl.droidsonroids.toast.data.dto

import java.util.*

data class EventDto(
        val id: Long,
        val title: String,
        val date: Date,
        val coverImages: List<ImageDto>
)