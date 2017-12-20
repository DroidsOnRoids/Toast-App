package pl.droidsonroids.toast.utils

sealed class NavigationRequest {
    object SpeakersSearch : NavigationRequest()
    data class SpeakerDetails(val id: Long) : NavigationRequest()
}