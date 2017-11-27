package pl.droidsonrioids.toast.data.model

data class Events(
        val featuredEvent: EventDetails,
        val lastEvents: List<Event>
)