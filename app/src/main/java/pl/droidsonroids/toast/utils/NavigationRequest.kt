package pl.droidsonroids.toast.utils

sealed class NavigationRequest {
    object SpeakersSearch : NavigationRequest()
    data class EventDetails(val id: Long) : NavigationRequest()
    // TODO: TOA-44 Add other navigation requests
}