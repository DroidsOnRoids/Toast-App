package pl.droidsonroids.toast.app.utils.managers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import pl.droidsonroids.toast.utils.SortingType
import javax.inject.Inject

class FirebaseAnalyticsManager @Inject constructor(private val bundle: Bundle, private val firebaseAnalytics: FirebaseAnalytics) {

    fun logUpcomingEventFacebookAttendEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_BUTTON, facebookId.let { putFacebookId(it) })
    }

    fun logUpcomingFacebookAttendSuccessEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_SUCCESS, putFacebookId(facebookId))
    }

    fun logUpcomingEventMeetupPlaceEvent() {
        firebaseAnalytics.logEvent(EventTracking.Events.MEETUP_PLACE, null)
    }

    fun logEventDetailsFacebookAttendEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.ATTEND_BUTTON, putFacebookId(facebookId))
    }

    fun logEventDetailsFacebookAttendSuccessEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.ATTEND_SUCCESS, putFacebookId(facebookId))
    }

    fun logEventDetailsReadMoreEvent(lectureName: String) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.READ_MORE, putLectureName(lectureName))
    }

    fun logEventDetailsShowSpeakerEvent(speakerId: Long) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.SHOW_SPEAKER, putSpeakerId(speakerId))
    }

    fun logEventDetailsMeetupPlaceEvent() {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.MEETUP_PLACE, null)
    }

    fun logEventDetailsSeePhotosEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.EventDetails.SEE_PHOTOS, putEventId(eventId))
    }

    fun logSpeakersSortOptionEvent(sortingType: SortingType) {
        firebaseAnalytics.logEvent(EventTracking.Speakers.SORT_OPTION, putSortingType(sortingType))
    }

    fun logSpeakersShowSpeakerEvent(speakerName: String) {
        firebaseAnalytics.logEvent(EventTracking.Speakers.SHOW_SPEAKER, putSpeakerName(speakerName))
    }

    fun logSpeakersShowSearchEvent() {
        firebaseAnalytics.logEvent(EventTracking.Speakers.SHOW_SEARCH, null)
    }

    fun logSearchPhraseEvent(phrase: String) {
        firebaseAnalytics.logEvent(EventTracking.Search.PHRASE, putPhrase(phrase))
    }

    fun logSearchShowSpeakerEvent(speakerName: String) {
        firebaseAnalytics.logEvent(EventTracking.Search.SHOW_SPEAKER, putSpeakerName(speakerName))
    }

    fun logSpeakerDetailsReadMoreEvent(lectureName: String) {
        firebaseAnalytics.logEvent(EventTracking.SpeakerDetails.READ_MORE, putLectureName(lectureName))
    }

    fun logShowEventDetailsEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.Events.SHOW_EVENT_DET, putEventId(eventId))
    }

    fun logSpeakerDetailsEventTapEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.SpeakerDetails.SHOW_EVENT_DET, putEventId(eventId))
    }

    fun logEventDetailsTapContactEvent(contactOption: String) {
        firebaseAnalytics.logEvent(EventTracking.SpeakerDetails.CONTACTS, putContactOption(contactOption))
    }

    fun logContactSendClickEvent(topicName: String) {
        firebaseAnalytics.logEvent(EventTracking.Contact.SEND_MESSAGE, putTopicName(topicName))
    }

    fun logContactChooseTopicEvent(topicName: String) {
        firebaseAnalytics.logEvent(EventTracking.Contact.CHOOSE_TOPIC, putTopicName(topicName))
    }


    private fun putContactOption(contactOption: String): Bundle {
        bundle.putString(EventTracking.Key.CONTACT_OPTION_KEY, contactOption)
        return bundle
    }

    private fun putEventId(eventId: Long): Bundle {
        bundle.putLong(EventTracking.Key.EVENT_ID_KEY, eventId)
        return bundle
    }

    private fun putFacebookId(facebookId: String): Bundle {
        bundle.putString(EventTracking.Key.FACEBOOK_ID_KEY, facebookId)
        return bundle
    }

    private fun putSpeakerId(speakerId: Long): Bundle {
        bundle.putLong(EventTracking.Key.SPEAKER_ID_KEY, speakerId)
        return bundle
    }

    private fun putSpeakerName(speakerName: String): Bundle {
        bundle.putString(EventTracking.Key.SPEAKER_ID_KEY, speakerName)
        return bundle
    }

    private fun putLectureName(lectureName: String): Bundle {
        bundle.putString(EventTracking.Key.LECTURE_NAME_KEY, lectureName)
        return bundle
    }

    private fun putSortingType(sortingType: SortingType): Bundle {
        bundle.putString(EventTracking.Key.SORTING_TYPE_KEY, when (sortingType) {
            SortingType.ALPHABETICAL -> EventTracking.Key.ALPHABETICAL_SORTING
            SortingType.DATE -> EventTracking.Key.DATE_SORTING
        })
        return bundle
    }

    private fun putTopicName(topicName: String): Bundle {
        bundle.putString(EventTracking.Key.TOPIC_NAME_KEY, topicName)
        return bundle
    }

    private fun putPhrase(phrase: String): Bundle {
        bundle.putString(EventTracking.Key.PHRASE_KEY, phrase)
        return bundle
    }

    private object EventTracking {
        object Events {
            const val MEETUP_PLACE = "events_meetup_place"
            const val ATTEND_BUTTON = "events_attend_button"
            const val ATTEND_SUCCESS = "events_attend_success"
            const val SHOW_EVENT_DET = "events_show_event_det"
        }

        object EventDetails {
            const val ATTEND_BUTTON = "event_det_attend_button"
            const val ATTEND_SUCCESS = "event_det_attend_success"
            const val READ_MORE = "event_det_read_more"
            const val SHOW_SPEAKER = "event_det_show_speaker"
            const val MEETUP_PLACE = "event_det_meetup_place"
            const val SEE_PHOTOS = "event_det_see_photos"
        }

        object Speakers {
            const val SORT_OPTION = "speakers_sort_option"
            const val SHOW_SPEAKER = "speakers_show_speaker"
            const val SHOW_SEARCH = "speakers_show_search"
        }

        object Search {
            const val PHRASE = "search_phrase"
            const val TAGS = "search_tags"
            const val SHOW_SPEAKER = "search_show_speaker"
        }

        object SpeakerDetails {
            const val CONTACTS = "speaker_det_contact"
            const val READ_MORE = "speaker_det_read_more"
            const val SLIDES = "speaker_det_slides"
            const val SHOW_EVENT_DET = "speaker_det_event_det"
        }

        object Contact {
            const val CHOOSE_TOPIC = "contact_choose_topic"
            const val SEND_MESSAGE = "contact_send_message"
        }

        object Key {
            const val EVENT_ID_KEY = "eventId"
            const val FACEBOOK_ID_KEY = "facebookId"
            const val LECTURE_NAME_KEY = "lectureName"
            const val SPEAKER_ID_KEY = "speakerName"
            const val PHRASE_KEY = "phrase"
            const val TOPIC_NAME_KEY = "contactType"

            const val SORTING_TYPE_KEY = "sortingType"
            const val DATE_SORTING = "date"
            const val ALPHABETICAL_SORTING = "alphabet"
            const val CONTACT_OPTION_KEY = "contactOption"
        }
    }
}