package pl.droidsonrioids.toast.data.dto

import pl.droidsonrioids.toast.data.Page

data class SplitEvents(
        val upcomingEvent: EventDetailsDto,
        val previousEvents: Page<EventDto>
)