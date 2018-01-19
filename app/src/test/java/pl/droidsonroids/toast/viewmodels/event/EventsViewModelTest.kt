package pl.droidsonroids.toast.viewmodels.event

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.internal.operators.maybe.MaybeJust
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.testPreviousEvents
import pl.droidsonroids.toast.testSplitEvents
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest

@RunWith(MockitoJUnitRunner::class)
class EventsViewModelTest {
    @Mock
    lateinit var eventsRepository: EventsRepository
    lateinit var eventsViewModel: EventsViewModel


    @Test
    fun shouldReturnFeaturedEvent() {
        whenever(eventsRepository.getEvents()).thenReturn(MaybeJust.just(testSplitEvents))
        eventsViewModel = EventsViewModel(eventsRepository)
        val upcomingEventViewModel = eventsViewModel.upcomingEvent.get()

        assertThat(upcomingEventViewModel, notNullValue())
        assertThat(upcomingEventViewModel.id, equalTo(testEventDetails.id))
        assertThat(upcomingEventViewModel.title, equalTo(testEventDetails.title))
    }

    @Test
    fun shouldReturnSingletonPreviousEventsList() {
        whenever(eventsRepository.getEvents()).thenReturn(MaybeJust.just(testSplitEvents))
        val eventsViewModel = EventsViewModel(eventsRepository)

        val previousEvents = eventsViewModel.previousEventsSubject.value

        assertThat(previousEvents.size, equalTo(1))
        val previousEventViewModel = (previousEvents.first() as? State.Item)?.item
        val testPreviousApiEvent = testPreviousEvents.first()
        assertThat(previousEventViewModel?.id, equalTo(testPreviousApiEvent.id))
        assertThat(previousEventViewModel?.title, equalTo(testPreviousApiEvent.title))
    }

    @Test
    fun shouldReturnSuccessLoadingStatus() {
        whenever(eventsRepository.getEvents()).thenReturn(MaybeJust.just(testSplitEvents))
        eventsViewModel = EventsViewModel(eventsRepository)
        val eventsLoadingStatus = eventsViewModel.loadingStatus

        assertThat(eventsLoadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldReturnErrorLoadingStatus() {
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.error(Throwable()))
        eventsViewModel = EventsViewModel(eventsRepository)
        val eventsLoadingStatus = eventsViewModel.loadingStatus

        assertThat(eventsLoadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }


    @Test
    fun shouldRequestNavigationToPreviousEventDetails() {
        whenever(eventsRepository.getEvents()).thenReturn(Maybe.just(testSplitEvents))
        eventsViewModel = EventsViewModel(eventsRepository)
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
        whenever(eventsRepository.getEvents()).thenReturn(MaybeJust.just(testSplitEvents))
        eventsViewModel = EventsViewModel(eventsRepository)
        val testObserver = eventsViewModel.navigationSubject.test()

        eventsViewModel.upcomingEvent.get().onClick()

        testObserver.assertValue {
            it is NavigationRequest.EventDetails
                    && it.id == testEventDetails.id
        }
    }

}