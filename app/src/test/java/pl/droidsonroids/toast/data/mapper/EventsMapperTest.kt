package pl.droidsonroids.toast.data.mapper

import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.testImageDto
import pl.droidsonroids.toast.viewmodels.event.EventItemViewModel
import java.util.*

class EventsMapperTest {
    @Test
    fun shouldMapApiEventDetailsToDto() {
        val testCoverImages = testEventDetails.coverImages.map { it.toDto() }
        val testTalks = testEventDetails.eventTalks.map { it.toDto() }
        val testPhotos = testEventDetails.photos.map { it.toDto() }
        with(testEventDetails.toDto()) {
            assertThat(id, equalTo(testEventDetails.id))
            assertThat(title, equalTo(testEventDetails.title))
            assertThat(date, equalTo(testEventDetails.date))
            assertThat(facebookId, equalTo(testEventDetails.facebookId))
            assertThat(placeName, equalTo(testEventDetails.placeName))
            assertThat(placeStreet, equalTo(testEventDetails.placeStreet))
            assertThat(coverImages, equalTo(testCoverImages))
            assertThat(talks, equalTo(testTalks))
            assertThat(photos, equalTo(testPhotos))
        }
    }

    @Test
    fun shouldMapEventItemViewModelToDto() {
        EventItemViewModel(
                id = 1L,
                title = "title",
                date = Date(),
                coverImage = testImageDto,
                onEventClick = mock(),
                onCoverLoadingFinish = mock()
        ).run {
            val eventDto = toDto()
            assertThat(eventDto.id, equalTo(id))
            assertThat(eventDto.title, equalTo(title))
            assertThat(eventDto.date, equalTo(date))
            assertThat(eventDto.coverImages, equalTo(listOfNotNull(coverImage)))
        }
    }
}