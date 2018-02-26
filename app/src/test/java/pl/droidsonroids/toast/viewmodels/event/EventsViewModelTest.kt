package pl.droidsonroids.toast.viewmodels.event

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.subjects.PublishSubject
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.dto.event.SplitEvents
import pl.droidsonroids.toast.data.enums.ParentView
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.testPreviousEvents
import pl.droidsonroids.toast.testSplitEvents
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.facebook.AttendViewModel
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class EventsViewModelTest {
    @Mock
    lateinit var eventsRepository: EventsRepository
    @Mock
    lateinit var loginStateWatcher: LoginStateWatcher
    @Mock
    lateinit var attendViewModel: AttendViewModel

    lateinit var eventsViewModel: EventsViewModel


    @Test
    fun shouldReturnFeaturedEvent() {
        setUpWith(Maybe.just(testSplitEvents))
        val upcomingEventViewModel = eventsViewModel.upcomingEvent.get()

        checkIsUpcomingEventLoaded(upcomingEventViewModel)
    }

    @Test
    fun shouldReturnSingletonPreviousEventsList() {
        setUpWith(Maybe.just(testSplitEvents))

        val previousEvents = eventsViewModel.previousEventsSubject.value

        checkIsPreviousEventsLoaded(previousEvents)
    }

    @Test
    fun shouldReturnSuccessLoadingStatus() {
        setUpWith(Maybe.just(testSplitEvents))
        val eventsLoadingStatus = eventsViewModel.loadingStatus

        assertThat(eventsLoadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldReturnErrorLoadingStatus() {
        setUpWith(Maybe.error(IOException()))
        val eventsLoadingStatus = eventsViewModel.loadingStatus

        assertThat(eventsLoadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }

    @Test
    fun shouldRequestNavigationToPreviousEventDetails() {
        setUpWith(Maybe.just(testSplitEvents))
        val previousEventsState = eventsViewModel.previousEventsSubject.value.firstOrNull() as? State.Item
        val testApiEvent = testPreviousEvents.first()
        val testObserver = eventsViewModel.navigationSubject.test()

        previousEventsState?.item?.onClick()

        testObserver.assertValue {
            it is NavigationRequest.EventDetails
                    && it.id == testApiEvent.id
        }
    }

    @Test
    fun shouldRequestNavigationToFeaturedEventDetails() {
        setUpWith(Maybe.just(testSplitEvents))
        val testObserver = eventsViewModel.navigationSubject.test()

        eventsViewModel.upcomingEvent.get().onEventClick()

        testObserver.assertValue {
            it is NavigationRequest.EventDetails
                    && it.id == testEventDetails.id
        }
    }


    @Test
    fun shouldRequestNavigationToPhotos() {
        setUpWith(Maybe.just(testSplitEvents))
        val testObserver = eventsViewModel.navigationSubject.test()

        eventsViewModel.upcomingEvent.get().onPhotosClick()

        testObserver.assertValue {
            it is NavigationRequest.Photos
                    && it.eventId == testEventDetails.id
                    && it.photos.first() == testEventDetails.photos.first().toDto()
                    && it.parentView == ParentView.HOME
        }
    }


    @Test
    fun shouldRequestNavigationToFeaturedEventLocation() {
        setUpWith(Maybe.just(testSplitEvents))
        val testObserver = eventsViewModel.navigationSubject.test()

        eventsViewModel.upcomingEvent.get().onLocationClick()

        testObserver.assertValue {
            it is NavigationRequest.Map
                    && it.coordinatesDto == testEventDetails.placeCoordinates.toDto()
                    && it.placeName == testEventDetails.placeName
        }
    }

    @Test
    fun shouldRefreshPreviousEvents() {
        setUpWith(Maybe.just(testSplitEvents.copy(previousEvents = Page(emptyList(), 1, 1))))
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.just(testSplitEvents))

        eventsViewModel.refresh()

        val previousEvents = eventsViewModel.previousEventsSubject.value
        checkIsPreviousEventsLoaded(previousEvents)
    }

    @Test
    fun shouldRefreshUpcomingEvent() {
        testSplitEvents.run {
            setUpWith(Maybe.just(copy(upcomingEvent = upcomingEvent.copy(id = -1))))
        }
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.just(testSplitEvents))

        eventsViewModel.refresh()

        val upcomingEventViewModel = eventsViewModel.upcomingEvent.get()
        checkIsUpcomingEventLoaded(upcomingEventViewModel)
    }

    @Test
    fun shouldHideSwipeRefreshLoaderWhenDataRefreshed() {
        setUpWith(Maybe.just(testSplitEvents.copy(previousEvents = Page(emptyList(), 1, 1))))
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.just(testSplitEvents))
        val testObserver = eventsViewModel.isSwipeRefreshLoaderVisibleSubject.test()

        eventsViewModel.refresh()

        testObserver.assertValue { !it }
    }

    @Test
    fun shouldRetainUpcomingEventWhenRefreshFailed() {
        setUpWith(Maybe.just(testSplitEvents))
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.error(IOException()))

        eventsViewModel.refresh()

        val upcomingEventViewModel = eventsViewModel.upcomingEvent.get()
        checkIsUpcomingEventLoaded(upcomingEventViewModel)
    }

    @Test
    fun shouldRetainPreviousEventsWhenRefreshFailed() {
        setUpWith(Maybe.just(testSplitEvents))
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.error(IOException()))

        eventsViewModel.refresh()

        val previousEvents = eventsViewModel.previousEventsSubject.value
        checkIsPreviousEventsLoaded(previousEvents)
    }

    @Test
    fun shouldRequestSnackBarWhenRefreshFailed() {
        setUpWith(Maybe.just(testSplitEvents))
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.error(IOException()))
        val testObserver = eventsViewModel.navigationSubject.test()

        eventsViewModel.refresh()

        testObserver.assertValue { it is NavigationRequest.SnackBar }
    }

    @Test
    fun shouldHideSwipeRefreshLoaderWhenRefreshFailed() {
        setUpWith(Maybe.just(testSplitEvents))
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.error(IOException()))
        val testObserver = eventsViewModel.isSwipeRefreshLoaderVisibleSubject.test()

        eventsViewModel.refresh()

        testObserver.assertValue { !it }
    }

    private fun setUpWith(maybe: Maybe<SplitEvents>) {
        whenever(eventsRepository.getEvents()).thenReturn(maybe)
        whenever(attendViewModel.navigationRequests).thenReturn(PublishSubject.create())
        eventsViewModel = EventsViewModel(loginStateWatcher, attendViewModel, eventsRepository, analyticsEventTracker = mock())
    }

    private fun checkIsPreviousEventsLoaded(previousEvents: List<State<EventItemViewModel>>) {
        assertThat(previousEvents.size, equalTo(1))
        val previousEventViewModel = (previousEvents.first() as? State.Item)?.item
        val testPreviousApiEvent = testPreviousEvents.first()
        assertThat(previousEventViewModel?.id, equalTo(testPreviousApiEvent.id))
        assertThat(previousEventViewModel?.title, equalTo(testPreviousApiEvent.title))
    }

    private fun checkIsUpcomingEventLoaded(upcomingEventViewModel: UpcomingEventViewModel) {
        assertThat(upcomingEventViewModel, notNullValue())
        assertThat(upcomingEventViewModel.id, equalTo(testEventDetails.id))
        assertThat(upcomingEventViewModel.title, equalTo(testEventDetails.title))
    }
}
