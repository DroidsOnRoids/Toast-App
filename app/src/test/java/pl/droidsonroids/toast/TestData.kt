package pl.droidsonroids.toast

import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.api.ApiCoordinates
import pl.droidsonroids.toast.data.api.ApiEvent
import pl.droidsonroids.toast.data.api.ApiEventDetails
import pl.droidsonroids.toast.data.api.ApiImage
import pl.droidsonroids.toast.data.dto.SplitEvents
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.utils.Consts
import java.text.SimpleDateFormat
import java.util.*

val testDate: Date = SimpleDateFormat(Consts.DATE_PATTERN).parse("1.12.2017")

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
