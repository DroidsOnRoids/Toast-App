package pl.droidsonrioids.toast.data.api

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonrioids.toast.managers.EventsManager
import pl.droidsonrioids.toast.managers.EventsManagerImpl
import pl.droidsonrioids.toast.rule.RxPluginSchedulerRule
import pl.droidsonrioids.toast.services.EventService
import pl.droidsonrioids.toast.testEventDetails
import pl.droidsonrioids.toast.testPreviousEvents

@RunWith(MockitoJUnitRunner::class)
class EventsManagerImplTest {

    @Mock lateinit var eventService: EventService
    lateinit var eventsManager: EventsManager

    @get:Rule
    val rxPluginSchedulerRule = RxPluginSchedulerRule()

    @Before
    fun setUp() {
        eventsManager = EventsManagerImpl(eventService)
    }

    @Test
    fun shouldReturnUpcomingEventAndEmptyPreviousEventsList() {
        val eventDetail = Single.just(EventDetailsResponse(testEventDetails))
        whenever(eventService.getEvent(any())).thenReturn(eventDetail)
        val eventsList = Single.just(EventsResponse(testPreviousEvents, 1))
        whenever(eventService.getEvents()).thenReturn(eventsList)
        val testObserver = eventsManager.getEvents().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        testObserver.assertValue { it.previousEvents.items.isEmpty() && it.upcomingEvent == testEventDetails }
    }

    @Test
    fun shouldReturnNothingWhenEmptyList() {
        val eventsList = Single.just(EventsResponse(listOf(), 1))
        whenever(eventService.getEvents()).thenReturn(eventsList)
        val testObserver = eventsManager.getEvents().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertNoValues()
    }


    @Test
    fun shouldReturnPreviousEventsPage() {
        val pageCount = 1
        val pageNo = 1
        val eventsList = Single.just(EventsResponse(testPreviousEvents, pageCount))
        whenever(eventService.getEvents(page = pageNo)).thenReturn(eventsList)
        val testObserver = eventsManager.getEventsPage(pageNo).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue { it.items == testPreviousEvents && it.pageNo == pageNo && it.pageCount == pageCount }
    }
}