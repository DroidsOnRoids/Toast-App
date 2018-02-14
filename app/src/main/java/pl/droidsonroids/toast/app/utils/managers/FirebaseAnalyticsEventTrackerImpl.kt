package pl.droidsonroids.toast.app.utils.managers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import pl.droidsonroids.toast.app.utils.extensions.*
import pl.droidsonroids.toast.utils.EventTracking
import pl.droidsonroids.toast.utils.SortingType
import javax.inject.Inject

class FirebaseAnalyticsEventTrackerImpl @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) : FirebaseAnalyticsEventTracker {

    override fun logUpcomingEventFacebookAttendEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_BUTTON, Bundle().putFacebookId(facebookId))
    }

    override fun logUpcomingEventFacebookAttendSuccessEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_SUCCESS, Bundle().putFacebookId(facebookId))
    }

    override fun logUpcomingEventTapMeetupPlaceEvent() {
        firebaseAnalytics.logEvent(EventTracking.Events.MEETUP_PLACE, null)
    }

    override fun logEventDetailsFacebookAttendEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.ATTEND_BUTTON, Bundle().putFacebookId(facebookId))
    }

    override fun logEventDetailsFacebookAttendSuccessEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.ATTEND_SUCCESS, Bundle().putFacebookId(facebookId))
    }

    override fun logEventDetailsReadMoreEvent(lectureName: String) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.READ_MORE, Bundle().putLectureName(lectureName))
    }

    override fun logEventDetailsShowSpeakerEvent(speakerId: Long) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.SHOW_SPEAKER, Bundle().putSpeakerId(speakerId))
    }

    override fun logEventDetailsTapMeetupPlaceEvent() {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.MEETUP_PLACE, null)
    }

    override fun logEventDetailsSeePhotosEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.SEE_PHOTOS, Bundle().putEventId(eventId))
    }

    override fun logSpeakersChooseSortOptionEvent(sortingType: SortingType) {
        firebaseAnalytics.logEvent(EventTracking.Speakers.SORT_OPTION, Bundle().putSortingType(sortingType))
    }

    override fun logSpeakersShowSpeakerEvent(speakerName: String) {
        firebaseAnalytics.logEvent(EventTracking.Speakers.SHOW_SPEAKER, Bundle().putSpeakerName(speakerName))
    }

    override fun logSpeakersShowSearchEvent() {
        firebaseAnalytics.logEvent(EventTracking.Speakers.SHOW_SEARCH, null)
    }

    override fun logSearchPhraseEvent(phrase: String) {
        firebaseAnalytics.logEvent(EventTracking.Search.PHRASE, Bundle().putPhrase(phrase))
    }

    override fun logSearchShowSpeakerEvent(speakerName: String) {
        firebaseAnalytics.logEvent(EventTracking.Search.SHOW_SPEAKER, Bundle().putSpeakerName(speakerName))
    }

    override fun logSpeakerDetailsReadMoreEvent(lectureName: String) {
        firebaseAnalytics.logEvent(EventTracking.SpeakerDetails.READ_MORE, Bundle().putLectureName(lectureName))
    }

    override fun logEventsShowEventDetailsEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.Events.SHOW_EVENT_DET, Bundle().putEventId(eventId))
    }

    override fun logSpeakerDetailsEventTapEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.SpeakerDetails.SHOW_EVENT_DET, Bundle().putEventId(eventId))
    }

    override fun logEventDetailsTapLinkEvent(linkOption: String) {
        firebaseAnalytics.logEvent(EventTracking.SpeakerDetails.CONTACTS, Bundle().putLinkOption(linkOption))
    }

    override fun logContactSendClickEvent(topicName: String) {
        firebaseAnalytics.logEvent(EventTracking.Contact.SEND_MESSAGE, Bundle().putTopicName(topicName))
    }

    override fun logContactChooseTopicEvent(topicName: String) {
        firebaseAnalytics.logEvent(EventTracking.Contact.CHOOSE_TOPIC, Bundle().putTopicName(topicName))
    }
}