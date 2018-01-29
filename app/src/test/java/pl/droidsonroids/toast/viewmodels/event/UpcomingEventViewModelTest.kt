package pl.droidsonroids.toast.viewmodels.event

import org.junit.Before
import org.junit.Test
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.testImageDto

class UpcomingEventViewModelTest {
    private lateinit var upcomingEventViewModel: UpcomingEventViewModel
    private var isCallbackCaused = false

    @Before
    fun setUp() {
        upcomingEventViewModel = UpcomingEventViewModel(
                testEventDetails.id,
                testEventDetails.title,
                testEventDetails.date,
                testEventDetails.placeName,
                testEventDetails.placeStreet,
                testImageDto,
                listOf(testImageDto),
                testEventDetails.placeCoordinates.toDto(),
                ::testLocationClickCallback,
                ::seePhotosClickCallback,
                ::eventClickCallback
        )
        isCallbackCaused = false
    }

    @Test
    fun shouldCauseLocationClickCallback() {
        upcomingEventViewModel.onLocationClick()
        assert(isCallbackCaused)
    }


    @Test
    fun shouldCauseSeePhotosClickCallback() {
        upcomingEventViewModel.onPhotosClick()
        assert(isCallbackCaused)
    }


    @Test
    fun shouldCauseEventClickCallback() {
        upcomingEventViewModel.onEventClick()
        assert(isCallbackCaused)
    }


    private fun testLocationClickCallback(coordinates: CoordinatesDto, placeName: String) {
        isCallbackCaused = true
    }

    private fun seePhotosClickCallback(photoId: Long, imageList: List<ImageDto>) {
        isCallbackCaused = true
    }

    private fun eventClickCallback(eventId: Long) {
        isCallbackCaused = true
    }
}