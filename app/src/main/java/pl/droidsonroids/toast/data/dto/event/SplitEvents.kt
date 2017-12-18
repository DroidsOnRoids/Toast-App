package pl.droidsonroids.toast.data.dto.event

import pl.droidsonroids.toast.data.Page

data class SplitEvents(
        val upcomingEvent: EventDetailsDto,
        val previousEvents: Page<EventDto>
)