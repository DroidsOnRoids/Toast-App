package pl.droidsonroids.toast.utils

object Constants {
    object SearchMenuItem {
        const val SHOWN_OFFSET = 0f
        const val HIDDEN_OFFSET = -200f
        const val ANIM_DURATION_MILLIS = 200L
    }

    object Page {
        const val SIZE = 10
        const val FIRST = 1
    }

    object Date {
        const val PATTERN = "dd.MM.yyyy"
    }

    object ValidationPatterns {
        const val EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$"
        const val NAME = "^[a-zA-Z-0-9]+[\\-'\\s]?[a-zA-Z-0-9 ]+\$"
    }

    object TalkTransition {
        const val CARD = "card"
    }

    const val NO_ID = -1L

    object PhotoTransition {
        const val PHOTO = "photo"
    }

    object ClipDataLabel {
        const val EMAIL = "Email address"
    }

    object SortingQuery {
        const val ALPHABETICAL = "name"
        const val DATE = "talk_date"
    }

    object Facebook {
        val PERMISSIONS = listOf("rsvp_event")
        const val EVENT_URL: String = "https://www.facebook.com/events/"
    }

    object EventTracking {
        object Events {
            const val MEETUP_PLACE = "events_meetup_place"
            const val ATTEND_BUTTON = "events_attend_button"
            const val ATTEND_SUCCESS = "events_attend_success"
            const val SHOW_EVENT_DET = "events_show_event_det"
        }
    }
}