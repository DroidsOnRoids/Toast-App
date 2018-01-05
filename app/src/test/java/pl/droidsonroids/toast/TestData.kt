package pl.droidsonroids.toast

import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.api.ApiImage
import pl.droidsonroids.toast.data.api.event.ApiCoordinates
import pl.droidsonroids.toast.data.api.event.ApiEvent
import pl.droidsonroids.toast.data.api.event.ApiEventDetails
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.dto.event.SplitEvents
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

val testDate: Date = SimpleDateFormat(Constants.Date.PATTERN).parse("1.12.2017")

val testEventDetails = ApiEventDetails(
        1,
        "title",
        testDate,
        "facebookId",
        "placeName",
        "placeStreet",
        ApiCoordinates(51.1098206, 17.0251941),
        listOf(ApiImage("bigCoverImageFirst", "thumbCoverImageFirst")),
        listOf(ApiImage("bigImageFirst", "thumbImageFirst"))
)

val testPreviousEvents = listOf(
        ApiEvent(
                0,
                "titleFirst",
                testDate,
                listOf(ApiImage("bigImageFirst", "thumbImageFirst"))
        )
)

val testPreviousEventsPage = Page(testPreviousEvents.map { it.toDto() }, 1, 1)

val testSplitEvents = SplitEvents(testEventDetails.toDto(), testPreviousEventsPage)

val testSpeaker = ApiSpeaker(
        0,
        "name",
        "job",
        ApiImage("bigImageFirst", "thumbImageFirst"),
        "bio"
)
val testSpeakers = listOf(testSpeaker)

val testSpeakersPage = Page(testSpeakers.map { it.toDto() }, 1, 1)