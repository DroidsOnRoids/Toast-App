package pl.droidsonroids.toast.app.utils.managers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import pl.droidsonroids.toast.utils.SortingType
import javax.inject.Inject

private const val EVENT_ID_KEY = "eventId"
private const val FACEBOOK_ID_KEY = "facebookId"
private const val LECTURE_NAME_KEY = "lectureName"
private const val SPEAKER_ID_KEY = "speakerName"
private const val PHRASE_KEY = "phrase"
private const val TOPIC_NAME_KEY = "contactType"

private const val SORTING_TYPE_KEY = "sortingType"
private const val DATE_SORTING = "date"
private const val ALPHABETICAL_SORTING = "alphabet"
private const val CONTACT_TYPE_KEY = "contactType"

class FirebaseAnalyticsManager @Inject constructor(private val bundle: Bundle, private val firebaseAnalytics: FirebaseAnalytics) {

    fun logUpcomingEventFacebookAttendEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_BUTTON, facebookId.let { putFacebookId(it) })
    }

    fun logUpcomingFacebookAttendSuccessEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_SUCCESS, putFacebookId(facebookId))
    }

    fun logShowEventDetailsEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.Events.SHOW_EVENT_DET, putEventId(eventId))
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


    private fun putEventId(eventId: Long): Bundle {
        bundle.putLong(EVENT_ID_KEY, eventId)
        return bundle
    }

    private fun putFacebookId(facebookId: String): Bundle {
        bundle.putString(FACEBOOK_ID_KEY, facebookId)
        return bundle
    }

    private fun putSpeakerId(speakerId: Long): Bundle {
        bundle.putLong(SPEAKER_ID_KEY, speakerId)
        return bundle
    }

    private fun putSpeakerName(speakerName: String): Bundle {
        bundle.putString(SPEAKER_ID_KEY, speakerName)
        return bundle
    }

    private fun putLectureName(lectureName: String): Bundle {
        bundle.putString(LECTURE_NAME_KEY, lectureName)
        return bundle
    }

    private fun putSortingType(sortingType: SortingType): Bundle {
        bundle.putString(SORTING_TYPE_KEY, when (sortingType) {
            SortingType.ALPHABETICAL -> ALPHABETICAL_SORTING
            SortingType.DATE -> DATE_SORTING
        })
        return bundle
    }

    private fun putTopicName(topicName: String): Bundle {
        bundle.putString(TOPIC_NAME_KEY, topicName)
        return bundle
    }

    private fun putPhrase(phrase: String): Bundle {
        bundle.putString(PHRASE_KEY, phrase)
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
    }

}