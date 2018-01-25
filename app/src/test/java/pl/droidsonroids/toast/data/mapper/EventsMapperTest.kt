package pl.droidsonroids.toast.data.mapper

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.testEventDetails

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
}