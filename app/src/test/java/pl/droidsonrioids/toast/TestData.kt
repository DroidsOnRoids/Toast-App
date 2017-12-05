package pl.droidsonrioids.toast

import pl.droidsonrioids.toast.data.api.ApiCoordinates
import pl.droidsonrioids.toast.data.api.ApiEvent
import pl.droidsonrioids.toast.data.api.ApiEventDetails
import pl.droidsonrioids.toast.data.api.ApiImage
import pl.droidsonrioids.toast.data.dto.SplitEvents
import pl.droidsonrioids.toast.data.mapper.toDto
import pl.droidsonrioids.toast.data.model.Page
import pl.droidsonrioids.toast.utils.Consts
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

val testSplitEvents = SplitEvents(testEventDetails.toDto(), testPreviousEvents.map { it.toDto() })

val testPreviousEventsPage = Page(testPreviousEvents, 1, 1)
