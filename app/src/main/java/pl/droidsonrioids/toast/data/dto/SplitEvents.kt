package pl.droidsonrioids.toast.data.dto

data class SplitEvents(
        val upcomingEvent: EventDetailsDto,
        val lastEvents: List<EventDto>
)