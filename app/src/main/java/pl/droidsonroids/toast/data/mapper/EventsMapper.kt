package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.ApiEvent
import pl.droidsonroids.toast.data.api.ApiEventDetails
import pl.droidsonroids.toast.data.dto.EventDetailsDto
import pl.droidsonroids.toast.data.dto.EventDto
import pl.droidsonroids.toast.viewmodels.EventItemViewModel

fun ApiEventDetails.toDto(): EventDetailsDto {
    val imagesDto = coverImages.map { it.toDto() }
    return EventDetailsDto(
            id,
            title,
            date,
            facebookId,
            placeName,
            placeStreet,
            imagesDto
    )
}

fun ApiEvent.toDto(): EventDto {
    val imagesDto = coverImages.map { it.toDto() }
    return EventDto(id, title, date, imagesDto)
}

fun EventDto.toViewModel(onClick: (Long) -> Unit): EventItemViewModel {
    return EventItemViewModel(
            id,
            title,
            date,
            coverImages.firstOrNull(),
            onClick
    )
}