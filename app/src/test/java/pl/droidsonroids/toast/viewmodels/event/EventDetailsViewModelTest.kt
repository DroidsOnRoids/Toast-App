package pl.droidsonroids.toast.viewmodels.event

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.rxkotlin.toSingle
import io.reactivex.subjects.PublishSubject
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.data.enums.ParentView
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.testApiEventTalk
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.DelayViewModel
import pl.droidsonroids.toast.viewmodels.facebook.AttendViewModel

class EventDetailsViewModelTest : RxTestBase() {
    @Mock
    lateinit var eventsRepository: EventsRepository
    @Mock
    lateinit var attendViewModel: AttendViewModel
    @Mock
    lateinit var delayViewModel: DelayViewModel
    @Mock
    lateinit var analyticsEventTracker: AnalyticsEventTracker

    lateinit var eventDetailsViewModel: EventDetailsViewModel

    private val eventId: Long = 1

    @Before
    fun setUp() {
        whenever(attendViewModel.navigationRequests).thenReturn(PublishSubject.create())
        eventDetailsViewModel = EventDetailsViewModel(eventsRepository, attendViewModel, analyticsEventTracker, delayViewModel)
    }

    @Test
    fun shouldLoadEventDetails() {
        val testEventDetailsSingle = testEventDetails.toDto().toSingle()
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetailsSingle)
        whenever(delayViewModel.addLoadingDelay(testEventDetailsSingle)).thenReturn(testEventDetailsSingle)
        eventDetailsViewModel.init(eventId, null)

        assertEventDetails()
        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldLoadEventDetailsOnlyOnce() {
        val testEventDetailsSingle = testEventDetails.toDto().toSingle()
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetailsSingle)
        whenever(delayViewModel.addLoadingDelay(testEventDetailsSingle)).thenReturn(testEventDetailsSingle)
        eventDetailsViewModel.init(eventId, null)

        eventDetailsViewModel.init(eventId, null)

        verify(eventsRepository, times(1)).getEvent(eventId)
    }

    @Test
    fun shouldFailLoadEventDetails() {
        val error = Single.error<EventDetailsDto>(Exception())
        whenever(eventsRepository.getEvent(eventId)).thenReturn(error)
        whenever(delayViewModel.addLoadingDelay(error)).thenReturn(error)
        eventDetailsViewModel.init(eventId, null)

        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }

    @Test
    fun shouldRetryLoadEventDetails() {
        val error = Single.error<EventDetailsDto>(Exception())
        whenever(eventsRepository.getEvent(eventId)).thenReturn(error)
        whenever(delayViewModel.addLoadingDelay(error)).thenReturn(error)
        eventDetailsViewModel.init(eventId, null)

        val testEventDetailsSingle = testEventDetails.toDto().toSingle()
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetailsSingle)
        whenever(delayViewModel.addLoadingDelay(testEventDetailsSingle)).thenReturn(testEventDetailsSingle)

        eventDetailsViewModel.retryLoading()

        assertEventDetails()
        assertThat(eventDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldRequestNavigationToPhotos() {
        val testEventDetailsSingle = testEventDetails.toDto().toSingle()
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetailsSingle)
        whenever(delayViewModel.addLoadingDelay(testEventDetailsSingle)).thenReturn(testEventDetailsSingle)
        eventDetailsViewModel.init(eventId, null)
        val testPhotos = testEventDetails.photos.map { it.toDto() }

        val testObserver = eventDetailsViewModel.navigationSubject.test()

        eventDetailsViewModel.onPhotosClick()

        testObserver.assertValue(NavigationRequest.Photos(testPhotos, eventId, ParentView.EVENT_DETAILS))
    }

    @Test
    fun shouldRequestNavigationToEventLocation() {
        val testEventDetailsSingle = testEventDetails.toDto().toSingle()
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetailsSingle)
        whenever(delayViewModel.addLoadingDelay(testEventDetailsSingle)).thenReturn(testEventDetailsSingle)
        eventDetailsViewModel.init(eventId, null)

        val testObserver = eventDetailsViewModel.navigationSubject.test()

        eventDetailsViewModel.onLocationClick()

        testObserver.assertValue(NavigationRequest.Map(testEventDetails.placeCoordinates.toDto(), testEventDetails.placeName))
    }

    @Test
    fun shouldNotLoadFromCacheWhenTransitionEnds() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(eventId, null)

        eventDetailsViewModel.onTransitionEnd()

        assertThat(eventDetailsViewModel.loadFromCache.get(), equalTo(false))
    }

    @Test
    fun shouldNotifyWhenImageLoadingFinished() {
        whenever(eventsRepository.getEvent(eventId)).thenReturn(testEventDetails.toDto().toSingle())
        eventDetailsViewModel.init(eventId, null)
        val testObserver = eventDetailsViewModel.coverImageLoadingFinishedSubject.test()

        eventDetailsViewModel.onLoadingFinished()

        testObserver.assertValueCount(1)
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
        with(testApiEventTalk) {
            eventDetailsViewModel.eventSpeakersSubject
                    .flatMap { it.toObservable() }
                    .test()
                    .assertValue { it.id == id }
                    .assertValue { it.title == title }
                    .assertValue { it.description == it.description }
        }
    }
}