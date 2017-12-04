package pl.droidsonrioids.toast

import pl.droidsonrioids.toast.data.model.*
import pl.droidsonrioids.toast.viewmodels.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

val testDate: Date = SimpleDateFormat(DATE_PATTERN).parse("1.12.2017")

val testEventDetails = EventDetailsDto(
        1,
        "title",
        testDate,
        "facebookId",
        "placeName",
        "placeStreet",
        Coordinates(51.1098206, 17.0251941),
        listOf(Image("bigCoverImageFirst", "thumbCoverImageFirst")),
        listOf(Image("bigImageFirst", "thumbImageFirst"))
)

val testPreviousEvents = listOf(
        Event(
                0,
                "titleFirst",
                testDate,
                listOf(Image("bigImageFirst", "thumbImageFirst"))
        )
)

val testSplitEvents = SplitEvents(testEventDetails, testPreviousEvents)
