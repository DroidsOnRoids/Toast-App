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
import pl.droidsonrioids.toast.managers.EventsRepositoryImpl
import pl.droidsonrioids.toast.rule.RxPluginSchedulerRule
import pl.droidsonrioids.toast.services.EventService
import pl.droidsonrioids.toast.testEventDetails
import pl.droidsonrioids.toast.testPreviousEvents

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
        val eventsList = Single.just(EventsResponse(testPreviousEvents))
        whenever(eventService.getEvents()).thenReturn(eventsList)

        eventsRepository.getEvents()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue { it.lastEvents.isEmpty() && it.upcomingEvent == testEventDetails.toDto() }
    }

    @Test
    fun shouldReturnNothingWhenEmptyList() {
        val eventsList = Single.just(EventsResponse(listOf()))
        whenever(eventService.getEvents()).thenReturn(eventsList)

        eventsRepository.getEvents()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertNoValues()
    }
}