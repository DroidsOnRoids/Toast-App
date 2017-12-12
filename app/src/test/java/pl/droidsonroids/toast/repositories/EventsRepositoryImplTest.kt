package pl.droidsonroids.toast.repositories

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.api.event.EventDetailsResponse
import pl.droidsonroids.toast.data.api.event.EventsResponse
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.event.EventsRepositoryImpl
import pl.droidsonroids.toast.services.EventService
import pl.droidsonroids.toast.testEventDetails
import pl.droidsonroids.toast.testPreviousEvents
import pl.droidsonroids.toast.testPreviousEventsPage

class EventsRepositoryImplTest : RxTestBase() {

    @Mock
    lateinit var eventService: EventService
    @InjectMocks
    lateinit var eventsRepository: EventsRepositoryImpl

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