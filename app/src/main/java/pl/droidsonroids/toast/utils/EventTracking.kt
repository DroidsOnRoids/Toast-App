package pl.droidsonroids.toast.utils


object EventTracking {
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
        const val LINK_OPTION_KEY = "linkOption"
    }
}