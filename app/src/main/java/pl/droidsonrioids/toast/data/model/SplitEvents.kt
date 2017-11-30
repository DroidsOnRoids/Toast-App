package pl.droidsonrioids.toast.data.model

data class SplitEvents(
        val upcomingEvent: EventDetails,
        val lastEvents: List<Event>
)