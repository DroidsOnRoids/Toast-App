package pl.droidsonroids.toast.viewmodels.event

import android.databinding.ObservableField
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.enums.AttendStatus
import java.util.*


data class UpcomingEventViewModel(
        val id: Long,
        val title: String,
        val date: Date,
        val placeName: String,
        val placeStreet: String,
        val coverImage: ImageDto?,
        val photos: List<ImageDto>,
        val coordinates: CoordinatesDto,
        private val locationClickCallback: (CoordinatesDto, String) -> Unit,
        private val seePhotosCallback: (Long, List<ImageDto>) -> Unit,
        private val eventClickCallback: (Long) -> Unit,
        private val attendCallback: () -> Unit,
        val attendStatus: AttendStatus
) {

    val photosAvailable = ObservableField(photos.isNotEmpty())

    fun onEventClick() {
        eventClickCallback(id)
    }

    fun onPhotosClick() {
        seePhotosCallback(id, photos)
    }

    fun onLocationClick() {
        locationClickCallback(coordinates, placeName)
    }

    fun onAttendClick() {
        attendCallback()
    }
}