package pl.droidsonroids.toast.data.api

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.EventsRepositoryImpl
import pl.droidsonroids.toast.rule.RxPluginSchedulerRule
import pl.droidsonroids.toast.services.EventService
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.testPreviousEvents
import pl.droidsonroids.toast.testPreviousEventsPage

@RunWith(MockitoJUnitRunner::class)
class EventsRepositoryImplTest {

    @Mock
    lateinit var eventService: EventService
    @InjectMocks
    lateinit var eventsRepository: EventsRepositoryImpl

    @get:Rule
    val rxPluginSchedulerRule = RxPluginSchedulerRule()

    @Test
    fun shouldReturnUpcomingEventAndEmptyPreviousEventsList() {
        val eventDetail = Single.just(EventDetailsResponse(testEventDetails))
        whenever(eventService.getEvent(any())).thenReturn(eventDetail)
        val eventsList = Single.just(EventsResponse(testPreviousEvents, 1))
        whenever(eventService.getEvents()).thenReturn(eventsList)

        eventsRepository.getEvents()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue { it.previousEvents.items.isEmpty() && it.upcomingEvent == testEventDetails.toDto() }
    }

    @Test
    fun shouldReturnNothingWhenEmptyList() {
        val eventsList = Single.just(EventsResponse(listOf(), 1))
        whenever(eventService.getEvents()).thenReturn(eventsList)

        eventsRepository.getEvents()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertNoValues()
    }


    @Test
    fun shouldReturnPreviousEventsPage() {
        val allPagesCount = 1
        val pageNumber = 1
        val eventsList = Single.just(EventsResponse(testPreviousEvents, allPagesCount))
        whenever(eventService.getEvents(pageNumber = pageNumber)).thenReturn(eventsList)

        eventsRepository.getEventsPage(pageNumber)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue { it == testPreviousEventsPage }
    }
}