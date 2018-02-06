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
        val photos: List<ImageDto>,
        val coordinates: CoordinatesDto,
        private val locationClickCallback: (CoordinatesDto, String) -> Unit,
        private val seePhotosCallback: (Long, List<ImageDto>) -> Unit,
        private val eventClickCallback: (Long) -> Unit,
        private val attendCallback: () -> Unit
) {

    val photosAvailable get() = photos.isNotEmpty()
    val isPastEvent get() = date.isYesterdayOrEarlier

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
        if (!isPastEvent) {
            attendCallback()
        }
    }

    private val Date.isYesterdayOrEarlier
        get() = before(
                Calendar.getInstance().run {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    time
                }
        )
}