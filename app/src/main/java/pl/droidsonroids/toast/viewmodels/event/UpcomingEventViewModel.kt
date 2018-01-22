package pl.droidsonroids.toast.viewmodels.event

import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import java.util.*


class UpcomingEventViewModel(
        val id: Long,
        val title: String,
        val date: Date,
        val placeName: String,
        val placeStreet: String,
        val coverImage: ImageDto?,
        val coordinates: CoordinatesDto,
        private val locationClickCallback: (CoordinatesDto) -> Unit,
        private val action: (Long) -> Unit
) {
    fun onClick() {
        action(id)
    }

    fun onLocationClick() {
        locationClickCallback(coordinates)
    }
}