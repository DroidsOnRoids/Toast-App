package pl.droidsonroids.toast

import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.api.ApiImage
import pl.droidsonroids.toast.data.api.event.ApiCoordinates
import pl.droidsonroids.toast.data.api.event.ApiEvent
import pl.droidsonroids.toast.data.api.event.ApiEventDetails
import pl.droidsonroids.toast.data.api.event.ApiEventTalk
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.api.speaker.ApiSpeakerDetails
import pl.droidsonroids.toast.data.api.speaker.ApiSpeakerTalk
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.EventDto
import pl.droidsonroids.toast.data.dto.event.SplitEvents
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDetailsDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

val testDate: Date = SimpleDateFormat(Constants.Date.PATTERN).parse("1.12.2017")

val testApiEvent = ApiEvent(
        id = 0,
        title = "titleFirst",
        date = testDate,
        coverImages = listOf(ApiImage("bigImageFirst", "thumbImageFirst"))
)
val testPreviousEvents = listOf(testApiEvent)

val testPreviousEventsPage = Page(items = testPreviousEvents.map { it.toDto() }, pageNumber = 1, allPagesCount = 1)

val testSpeaker = ApiSpeaker(
        id = 0,
        name = "name",
        job = "job",
        avatar = ApiImage("bigImageFirst", "thumbImageFirst")
)

val testApiEventTalk = ApiEventTalk(
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
        eventTalks = listOf(testApiEventTalk)
)

val testSplitEvents = SplitEvents(upcomingEvent = testEventDetails.toDto(), previousEvents = testPreviousEventsPage)

val testSpeakers = listOf(testSpeaker)

val testSpeakersPage = Page(items = testSpeakers.map { it.toDto() }, pageNumber = 1, allPagesCount = 1)

val testImageDto = ImageDto(
        "originalSizeUrl",
        "thumbSizeUrl"
)

val testEventDto = EventDto(
        id = 0,
        title = "titleFirst",
        date = testDate,
        coverImages = listOf(ImageDto("bigImageFirst", "thumbImageFirst")))

val testSpeakerTalkDto = SpeakerTalkDto(
        id = 1,
        title = "title",
        description = "description",
        event = testEventDto
)

val testApiSpeakerTalk = ApiSpeakerTalk(
        id = 1,
        title = "title",
        description = "description",
        event = testApiEvent
)

val testSpeakerDetailsDto = SpeakerDetailsDto(
        id = 0,
        name = "name",
        job = "job",
        bio = "bio",
        avatar = ImageDto("bigAvatar", "thumbAvatar"),
        github = "github",
        email = "email",
        website = "website",
        twitter = "twitter",
        talks = listOf(testSpeakerTalkDto)
)

val testApiSpeakerDetails = ApiSpeakerDetails(
        id = 0,
        name = "name",
        job = "job",
        bio = "bio",
        avatar = ApiImage("bigAvatar", "thumbAvatar"),
        github = "github",
        email = "email",
        website = "website",
        twitter = "twitter",
        talks = listOf(testApiSpeakerTalk)
)