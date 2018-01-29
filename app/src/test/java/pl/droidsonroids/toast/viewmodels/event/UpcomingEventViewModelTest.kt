package pl.droidsonroids.toast.viewmodels.event

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.testImageDto

class UpcomingEventViewModelTest {
    private lateinit var upcomingEventViewModel: UpcomingEventViewModel
    private var isCallbackCaused = false

    @Test
    fun shouldCauseLocationClickCallback() {
        setTestUpcomingEventViewModel()
        isCallbackCaused = false

        upcomingEventViewModel.onLocationClick()
        assertThat(isCallbackCaused, equalTo(true))
    }

    @Test
    fun shouldCauseSeePhotosClickCallback() {
        setTestUpcomingEventViewModel()
        isCallbackCaused = false

        upcomingEventViewModel.onPhotosClick()
        assertThat(isCallbackCaused, equalTo(true))
    }


    @Test
    fun shouldCauseEventClickCallback() {
        setTestUpcomingEventViewModel()
        isCallbackCaused = false

        upcomingEventViewModel.onEventClick()
        assertThat(isCallbackCaused, equalTo(true))
    }

    @Test
    fun shouldMakePhotosAvailable() {
        setTestUpcomingEventViewModel()

        assertThat(upcomingEventViewModel.photosAvailable.get(), equalTo(true))
    }


    @Test
    fun shouldMakePhotosUnavailable() {
        setTestEmptyPhotosUpcomingEventViewModel()

        assertThat(upcomingEventViewModel.photosAvailable.get(), equalTo(false))
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

    private fun setTestUpcomingEventViewModel() {
        upcomingEventViewModel = UpcomingEventViewModel(
                id = testEventDetails.id,
                title = testEventDetails.title,
                date = testEventDetails.date,
                placeName = testEventDetails.placeName,
                placeStreet = testEventDetails.placeStreet,
                coverImage = testImageDto,
                photos = listOf(testImageDto),
                coordinates = testEventDetails.placeCoordinates.toDto(),
                locationClickCallback = ::testLocationClickCallback,
                seePhotosCallback = ::seePhotosClickCallback,
                eventClickCallback = ::eventClickCallback
        )
    }

    private fun setTestEmptyPhotosUpcomingEventViewModel() {
        upcomingEventViewModel = UpcomingEventViewModel(
                id = testEventDetails.id,
                title = testEventDetails.title,
                date = testEventDetails.date,
                placeName = testEventDetails.placeName,
                placeStreet = testEventDetails.placeStreet,
                coverImage = testImageDto,
                photos = listOf(),
                coordinates = testEventDetails.placeCoordinates.toDto(),
                locationClickCallback = ::testLocationClickCallback,
                seePhotosCallback = ::seePhotosClickCallback,
                eventClickCallback = ::eventClickCallback
        )
    }
}