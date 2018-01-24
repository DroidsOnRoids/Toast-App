package pl.droidsonroids.toast.viewmodels.event

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.rxkotlin.toSingle
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.enums.ParentView
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.testApiTalk
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest

class EventDetailsViewModelTest : RxTestBase() {
    @Mock
    lateinit var eventsRepository: EventsRepository
    @InjectMocks
    lateinit var eventDetailsViewModel: EventDetailsViewModel

    private val eventId: Long = 1

    @Test
    fun shouldLoadEventDetails() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(eventId)

        assertEventDetails()
        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldLoadEventDetailsOnlyOnce() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(eventId)

        eventDetailsViewModel.init(eventId)

        verify(eventsRepository, times(1)).getEvent(eventId)
    }

    @Test
    fun shouldFailLoadEventDetails() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(Single.error(Exception()))
        eventDetailsViewModel.init(eventId)

        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }

    @Test
    fun shouldRetryLoadEventDetails() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(Single.error(Exception()))
        eventDetailsViewModel.init(eventId)
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetails.toDto().toSingle())

        eventDetailsViewModel.retryLoading()

        assertEventDetails()
        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldRequestNavigationToPhotos() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(eventId)
        val testPhotos = testEventDetails.photos.map { it.toDto() }

        val testObserver = eventDetailsViewModel.navigationSubject.test()

        eventDetailsViewModel.onPhotosClick()

        testObserver.assertValue(NavigationRequest.Photos(testPhotos, eventId, ParentView.EVENT_DETAILS))
    }

    @Test
    fun shouldRequestNavigationToEventLocation() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(eventId)

        val testObserver = eventDetailsViewModel.navigationSubject.test()

        eventDetailsViewModel.onLocationClick()

        testObserver.assertValue(NavigationRequest.Map(testEventDetails.placeCoordinates.toDto(), testEventDetails.placeName))
    }

    private fun assertEventDetails() {
        assertThat(eventDetailsViewModel.title.get(), equalTo(testEventDetails.title))
        assertThat(eventDetailsViewModel.date.get(), equalTo(testEventDetails.date))
        assertThat(eventDetailsViewModel.placeName.get(), equalTo(testEventDetails.placeName))
        assertThat(eventDetailsViewModel.placeStreet.get(), equalTo(testEventDetails.placeStreet))
        assertThat(eventDetailsViewModel.coverImage.get(), equalTo(testEventDetails.coverImages.firstOrNull()?.toDto()))
        assertEventSpeakers()
    }

    private fun assertEventSpeakers() {
        with(testApiTalk) {
            eventDetailsViewModel.eventSpeakersSubject
                    .flatMap { it.toObservable() }
                    .test()
                    .assertValue { it.id == id }
                    .assertValue { it.title == title }
                    .assertValue { it.description == it.description }
        }
    }
}