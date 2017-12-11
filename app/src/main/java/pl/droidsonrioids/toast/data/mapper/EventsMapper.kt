package pl.droidsonrioids.toast.data.mapper

import pl.droidsonrioids.toast.data.api.ApiEvent
import pl.droidsonrioids.toast.data.api.ApiEventDetails
import pl.droidsonrioids.toast.data.dto.EventDetailsDto
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.viewmodels.EventItemViewModel

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