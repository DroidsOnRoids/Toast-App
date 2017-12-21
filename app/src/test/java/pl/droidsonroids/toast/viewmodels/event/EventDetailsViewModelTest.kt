package pl.droidsonroids.toast.viewmodels.event

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.utils.LoadingStatus

class EventDetailsViewModelTest : RxTestBase() {
    @Mock
    lateinit var eventsRepository: EventsRepository
    @InjectMocks
    lateinit var eventDetailsViewModel: EventDetailsViewModel

    @Test
    fun shouldLoadEventDetails() {
        val id: Long = 1
        whenever(eventsRepository.getEvent(id)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(id)

        assertEventDetails()
        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldLoadEventDetailsOnlyOnce() {
        val id: Long = 1
        whenever(eventsRepository.getEvent(id)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(id)

        eventDetailsViewModel.init(id)

        verify(eventsRepository, times(1)).getEvent(id)
    }

    @Test
    fun shouldFailLoadEventDetails() {
        val id: Long = 1
        whenever(eventsRepository.getEvent(id)).thenReturn(Single.error(Exception()))
        eventDetailsViewModel.init(id)

        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }

    @Test
    fun shouldRetryLoadEventDetails() {
        val id: Long = 1
        whenever(eventsRepository.getEvent(id)).thenReturn(Single.error(Exception()))
        eventDetailsViewModel.init(id)
        whenever(eventsRepository.getEvent(id)).thenReturn(testEventDetails.toDto().toSingle())

        eventDetailsViewModel.retryLoading()

        assertEventDetails()
        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    private fun assertEventDetails() {
        assertThat(eventDetailsViewModel.title.get(), equalTo(testEventDetails.title))
        assertThat(eventDetailsViewModel.date.get(), equalTo(testEventDetails.date))
        assertThat(eventDetailsViewModel.placeName.get(), equalTo(testEventDetails.placeName))
        assertThat(eventDetailsViewModel.placeStreet.get(), equalTo(testEventDetails.placeStreet))
        assertThat(eventDetailsViewModel.coverImage.get(), equalTo(testEventDetails.coverImages.firstOrNull()?.toDto()))
    }
}