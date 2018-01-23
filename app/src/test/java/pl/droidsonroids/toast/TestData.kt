package pl.droidsonroids.toast

import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.api.ApiImage
import pl.droidsonroids.toast.data.api.event.ApiCoordinates
import pl.droidsonroids.toast.data.api.event.ApiEvent
import pl.droidsonroids.toast.data.api.event.ApiEventDetails
import pl.droidsonroids.toast.data.api.event.ApiTalk
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.SplitEvents
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

val testDate: Date = SimpleDateFormat(Constants.Date.PATTERN).parse("1.12.2017")

val testPreviousEvents = listOf(
        ApiEvent(
                id = 0,
                title = "titleFirst",
                date = testDate,
                coverImages = listOf(ApiImage("bigImageFirst", "thumbImageFirst"))
        )
)

val testPreviousEventsPage = Page(items = testPreviousEvents.map { it.toDto() }, pageNumber = 1, allPagesCount = 1)

val testSpeaker = ApiSpeaker(
        id = 0,
        name = "name",
        job = "job",
        avatar = ApiImage("bigImageFirst", "thumbImageFirst"),
        bio = "bio"
)

val testApiTalk = ApiTalk(
        id = 0,
        title = "title",
        description = "description",
        speaker = testSpeaker
)

val testEventDetails = ApiEventDetails(
        id = 1,
        title = "title",
        date = testDate,
        facebookId = "facebookId",
        placeName = "placeName",
        placeStreet = "placeStreet",
        placeCoordinates = ApiCoordinates(51.1098206, 17.0251941),
        coverImages = listOf(ApiImage("bigCoverImageFirst", "thumbCoverImageFirst")),
        photos = listOf(ApiImage("bigImageFirst", "thumbImageFirst")),
        talks = listOf(testApiTalk)
)

val testSplitEvents = SplitEvents(upcomingEvent = testEventDetails.toDto(), previousEvents = testPreviousEventsPage)

val testSpeakers = listOf(testSpeaker)

val testSpeakersPage = Page(items = testSpeakers.map { it.toDto() }, pageNumber = 1, allPagesCount = 1)

val testImageDto = ImageDto(
        "originalSizeUrl",
        "thumbSizeUrl"
)