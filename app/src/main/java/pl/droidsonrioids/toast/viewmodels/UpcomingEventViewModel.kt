package pl.droidsonrioids.toast.viewmodels

import pl.droidsonrioids.toast.data.model.EventDetails


class UpcomingEventViewModel(private val eventDetails: EventDetails) : EventDetails by eventDetails {
    val coverImage = coverImages.firstOrNull()
}