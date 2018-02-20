package pl.droidsonroids.toast.app.utils.managers

import pl.droidsonroids.toast.utils.SortingType


interface AnalyticsEventTracker {

    fun logUpcomingEventFacebookAttendEvent(facebookId: String)

    fun logUpcomingEventFacebookAttendSuccessEvent(facebookId: String)

    fun logUpcomingEventTapMeetupPlaceEvent()

    fun logEventDetailsFacebookAttendEvent(facebookId: String)

    fun logEventDetailsFacebookAttendSuccessEvent(facebookId: String)

    fun logEventDetailsReadMoreEvent(lectureName: String)

    fun logEventDetailsShowSpeakerEvent(speakerName: String)

    fun logEventDetailsTapMeetupPlaceEvent()

    fun logEventDetailsSeePhotosEvent(eventId: Long)

    fun logSpeakersChooseSortOptionEvent(sortingType: SortingType)

    fun logSpeakersShowSpeakerEvent(speakerName: String)

    fun logSpeakersShowSearchEvent()

    fun logSearchPhraseEvent(phrase: String)

    fun logSearchShowSpeakerEvent(speakerName: String)

    fun logSpeakerDetailsReadMoreEvent(lectureName: String)

    fun logEventsShowEventDetailsEvent(eventId: Long)

    fun logSpeakerDetailsEventTapEvent(eventId: Long)

    fun logEventDetailsTapGithubEvent(link: String)

    fun logEventDetailsTapWebsiteEvent(link: String)

    fun logEventDetailsTapEmailEvent(link: String)

    fun logEventDetailsTapTwitterEvent(link: String)

    fun logContactSendClickEvent(topicName: String)

    fun logContactChooseTopicEvent(topicName: String)
}