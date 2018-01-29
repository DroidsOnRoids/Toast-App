package pl.droidsonroids.toast.utils

import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.dto.event.EventTalkDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.data.enums.ParentView

sealed class NavigationRequest {
    object SpeakersSearch : NavigationRequest()
    data class EventDetails(val id: Long) : NavigationRequest()
    data class SpeakerDetails(val id: Long, val eventId: Long = Constants.NO_ID) : NavigationRequest()
    object MessageSent : NavigationRequest()
    data class EventTalkDetails(val eventTalkDto: EventTalkDto, val eventId: Long) : NavigationRequest()
    data class SpeakerTalkDetails(val speakerTalkDto: SpeakerTalkDto) : NavigationRequest()
    object Close : NavigationRequest()
    data class Photos(val photos: List<ImageDto>, val eventId: Long, val parentView: ParentView) : NavigationRequest()
    data class SinglePhoto(val photos: List<ImageDto>, val position: Long) : NavigationRequest()
    data class Map(val coordinatesDto: CoordinatesDto, val placeName: String) : NavigationRequest()
    object ToggleImmersive : NavigationRequest()
}

