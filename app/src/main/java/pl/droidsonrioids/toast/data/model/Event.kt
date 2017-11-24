package pl.droidsonrioids.toast.data.model


data class Event(
        val id: Int,
        val title: String,
        val date: String,
        val facebook: Int,
        val placeName: String,
        val placeStreet: String,
        val placeCoordinates: Coordinates,
        val coverPhotos: List<String>
)