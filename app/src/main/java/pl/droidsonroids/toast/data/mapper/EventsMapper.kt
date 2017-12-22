package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.event.ApiEvent
import pl.droidsonroids.toast.data.api.event.ApiEventDetails
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.data.dto.event.EventDto
import pl.droidsonroids.toast.viewmodels.event.EventItemViewModel
import pl.droidsonroids.toast.viewmodels.event.UpcomingEventViewModel

fun ApiEventDetails.toDto(): EventDetailsDto {
    val imagesDto = coverImages.map { it.toDto() }
    return EventDetailsDto(
            id = id,
            title = title,
            date = date,
            facebookId = facebookId,
            placeName = placeName,
            placeStreet = placeStreet,
            coverImages = imagesDto
    )
}

fun ApiEvent.toDto(): EventDto {
    val imagesDto = coverImages.map { it.toDto() }
    return EventDto(
            id = id,
            title = title,
            date = date,
            coverImages = imagesDto
    )
}

fun EventDto.toViewModel(onClick: (Long) -> Unit): EventItemViewModel {
    return EventItemViewModel(
            id = id,
            title = title,
            date = date,
            coverImage = coverImages.firstOrNull(),
            action = onClick
    )
}

fun EventDetailsDto.toViewModel(onClick: (Long) -> Unit): UpcomingEventViewModel {
    return UpcomingEventViewModel(
            id = id,
            title = title,
            date = date,
            placeName = placeName,
            placeStreet = placeStreet,
            coverImage = coverImages.firstOrNull(),
            action = onClick
    )
}
