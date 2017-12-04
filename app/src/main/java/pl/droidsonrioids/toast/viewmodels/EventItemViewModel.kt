package pl.droidsonrioids.toast.viewmodels

import pl.droidsonrioids.toast.data.model.Event

class EventItemViewModel(private val event: Event, val onClick: () -> Unit) : Event by event {
    val coverImage = coverImages.firstOrNull()
}