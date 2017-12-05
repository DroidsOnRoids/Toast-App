package pl.droidsonrioids.toast.data.api

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonrioids.toast.data.mapper.toDto
import pl.droidsonrioids.toast.repositories.EventsRepositoryImpl
import pl.droidsonrioids.toast.rule.RxPluginSchedulerRule
import pl.droidsonrioids.toast.services.EventService
import pl.droidsonrioids.toast.testEventDetails
import pl.droidsonrioids.toast.testPreviousEvents
import pl.droidsonrioids.toast.testPreviousEventsPage

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
        val pageCount = 1
        val pageNo = 1
        val eventsList = Single.just(EventsResponse(testPreviousEvents, pageCount))
        whenever(eventService.getEvents(page = pageNo)).thenReturn(eventsList)

        eventsRepository.getEventsPage(pageNo)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue { it == testPreviousEventsPage }
    }
}