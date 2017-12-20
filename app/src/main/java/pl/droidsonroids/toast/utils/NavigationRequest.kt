package pl.droidsonroids.toast.utils

sealed class NavigationRequest {
    object SpeakersSearch : NavigationRequest()
    // TODO: TOA-44, TOA-57 Add other navigation requests
}