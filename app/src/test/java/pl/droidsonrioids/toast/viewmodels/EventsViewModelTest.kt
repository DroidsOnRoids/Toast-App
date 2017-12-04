package pl.droidsonrioids.toast.viewmodels

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.internal.operators.maybe.MaybeJust
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonrioids.toast.managers.EventsRepository
import pl.droidsonrioids.toast.testEventDetails
import pl.droidsonrioids.toast.testPreviousEvents
import pl.droidsonrioids.toast.testSplitEvents

@RunWith(MockitoJUnitRunner::class)
class EventsViewModelTest {
    @Mock
    lateinit var eventsRepository: EventsRepository

    @Test
    fun shouldReturnFeaturedEvent() {
        whenever(eventsRepository.getEvents()).thenReturn(MaybeJust.just(testSplitEvents))
        val eventsViewModel = EventsViewModel(eventsRepository)

        val upcomingEventViewModel = eventsViewModel.featuredEvent.get()

        assertNotNull(upcomingEventViewModel)
        assertThat(upcomingEventViewModel.id, equalTo(testEventDetails.id))
        assertThat(upcomingEventViewModel.title, equalTo(testEventDetails.title))
    }

    @Test
    fun shouldReturnSingletonPreviousEventsList() {
        whenever(eventsRepository.getEvents()).thenReturn(MaybeJust.just(testSplitEvents))
        val eventsViewModel = EventsViewModel(eventsRepository)

        val previousEvents = eventsViewModel.lastEvents

        assertTrue(previousEvents.isNotEmpty())
        assertThat(previousEvents, equalTo(testPreviousEvents))
    }

}