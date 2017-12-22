package pl.droidsonroids.toast.utils

sealed class NavigationRequest {
    object SpeakersSearch : NavigationRequest()
    data class EventDetails(val id: Long) : NavigationRequest()
    data class SpeakerDetails(val id: Long) : NavigationRequest()
}