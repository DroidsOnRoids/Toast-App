package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.event.ApiCoordinates
import pl.droidsonroids.toast.data.api.event.ApiEvent
import pl.droidsonroids.toast.data.api.event.ApiEventDetails
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.api.event.ApiEventTalk
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.data.dto.event.EventDto
import pl.droidsonroids.toast.data.dto.event.EventTalkDto
import pl.droidsonroids.toast.viewmodels.event.EventItemViewModel
import pl.droidsonroids.toast.viewmodels.event.EventSpeakerItemViewModel
import pl.droidsonroids.toast.viewmodels.event.UpcomingEventViewModel

fun ApiEventDetails.toDto(): EventDetailsDto {
    val coverImagesDto = coverImages.map { it.toDto() }
    val photosDto = photos.map { it.toDto() }
    val talksDto = eventTalks.map { it.toDto() }
    val coordinatesDto = placeCoordinates.toDto()
    return EventDetailsDto(
            id = id,
            title = title,
            date = date,
            facebookId = facebookId,
            placeName = placeName,
            placeStreet = placeStreet,
            coverImages = coverImagesDto,
            talks = talksDto,
            photos = photosDto,
            coordinates = coordinatesDto
    )
}

fun ApiCoordinates.toDto(): CoordinatesDto {
    return CoordinatesDto(
            latitude = latitude,
            longitude = longitude
    )
}

fun ApiEventTalk.toDto(): EventTalkDto {
    return EventTalkDto(
            id = id,
            title = title,
            description = description,
            speaker = speaker.toDto()
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

fun EventItemViewModel.toDto(): EventDto {
    return EventDto(
            id,
            title,
            date,
            listOfNotNull(coverImage)
    )
}

fun EventDetailsDto.toViewModel(onLocationClick: (CoordinatesDto, String) -> Unit, onClick: (Long) -> Unit): UpcomingEventViewModel {
    return UpcomingEventViewModel(
            id = id,
            title = title,
            date = date,
            placeName = placeName,
            placeStreet = placeStreet,
            coverImage = coverImages.firstOrNull(),
            coordinates = coordinates,
            action = onClick,
            locationClickCallback = onLocationClick
    )
}

fun EventTalkDto.toViewModel(onReadMore: (EventSpeakerItemViewModel) -> Unit, onSpeakerClick: (Long) -> Unit): EventSpeakerItemViewModel {
    return EventSpeakerItemViewModel(
            id = id,
            title = title,
            description = description,
            speakerItemViewModel = speaker.toViewModel(onSpeakerClick),
            readMoreAction = onReadMore
    )
}

fun EventSpeakerItemViewModel.toDto(): EventTalkDto {
    return EventTalkDto(
            id = id,
            title = title,
            description = description,
            speaker = speakerItemViewModel.toDto()
    )
}
