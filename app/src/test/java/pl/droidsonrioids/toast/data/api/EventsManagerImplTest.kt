package pl.droidsonrioids.toast.data.api

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonrioids.toast.data.model.*
import pl.droidsonrioids.toast.rule.RxPluginSchedulerRule
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class EventsManagerImplTest {

    @Mock lateinit var eventService: EventService
    lateinit var eventsManagerImpl: EventsManager
    private val currentDate = Calendar.getInstance().time

    @get:Rule
    val rxPluginSchedulerRule = RxPluginSchedulerRule()

    @Before
    fun setUp() {
        val eventsList = Single.just(createTestEventsResponse())
        val eventDetail = Single.just(createTestEventsDetailsResponse())
        eventsManagerImpl = EventsManagerImpl(eventService)

        whenever(eventService.getEvents()).thenReturn(eventsList)
        whenever(eventService.getEvent(0)).thenReturn(eventDetail)
    }

    @Test
    fun shouldEventsObject() {
        val testObserver = eventsManagerImpl.getEvents().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun shouldReturnEventDetails() {
        val testObserver = eventsManagerImpl.getEvents().test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    private fun createTestEventsDetailsResponse(): EventDetailsResponse {
        return EventDetailsResponse(EventDetailsImpl(
                0,
                "titleFirst",
                currentDate,
                "123456789",
                "placeName",
                "placeStreet",
                Coordinates(0.0, 0.0),
                listOf(Image("bigImageFirst", "thumbImageFirst")),
                listOf(Image("bigImageFirst", "thumbImageFirst")))
        )
    }

    private fun createTestEventsResponse(): EventsResponse {
        return EventsResponse(listOf(
                Event(
                        0,
                        "titleFirst",
                        currentDate,
                        listOf(Image("bigImageFirst", "thumbImageFirst"))),
                Event(
                        1,
                        "titleSecond",
                        currentDate,
                        listOf(Image("bigImageSecond", "thumbImageSecond")))
        ))
    }

}