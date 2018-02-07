package pl.droidsonroids.toast.viewmodels.event

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.upcomingEventViewModelWithPhotos
import pl.droidsonroids.toast.upcomingEventViewModelWithoutPhotos

class UpcomingEventViewModelTest {
    private lateinit var upcomingEventViewModel: UpcomingEventViewModel

    @Test
    fun shouldMakePhotosAvailable() {
        upcomingEventViewModel = upcomingEventViewModelWithPhotos

        assertThat(upcomingEventViewModelWithPhotos.photosAvailable, equalTo(true))
    }


    @Test
    fun shouldMakePhotosUnavailable() {
        upcomingEventViewModel = upcomingEventViewModelWithoutPhotos

        assertThat(upcomingEventViewModel.photosAvailable, equalTo(false))
    }
}