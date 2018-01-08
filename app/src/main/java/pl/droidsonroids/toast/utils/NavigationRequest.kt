package pl.droidsonroids.toast.utils

import pl.droidsonroids.toast.data.dto.event.TalkDto

sealed class NavigationRequest {
    object SpeakersSearch : NavigationRequest()
    data class EventDetails(val id: Long) : NavigationRequest()
    data class SpeakerDetails(val id: Long) : NavigationRequest()
    data class TalkDetails(val talkDto: TalkDto) : NavigationRequest()
}