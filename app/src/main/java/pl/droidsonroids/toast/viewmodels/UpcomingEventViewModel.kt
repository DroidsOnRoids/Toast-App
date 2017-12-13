package pl.droidsonroids.toast.viewmodels

import pl.droidsonroids.toast.data.dto.EventDetailsDto
import pl.droidsonroids.toast.data.dto.ImageDto
import java.util.*


class UpcomingEventViewModel(
        val id: Int,
        val title: String,
        val date: Date,
        val placeName: String,
        val placeStreet: String,
        val coverImage: ImageDto?
) {

    companion object {
        fun create(eventDetailsDto: EventDetailsDto): UpcomingEventViewModel {
            with(eventDetailsDto) {
                return UpcomingEventViewModel(
                        id,
                        title,
                        date,
                        placeName,
                        placeStreet,
                        coverImages.firstOrNull()
                )
            }
        }
    }
}