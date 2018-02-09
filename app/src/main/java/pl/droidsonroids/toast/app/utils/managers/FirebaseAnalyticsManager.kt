package pl.droidsonroids.toast.app.utils.managers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject


private const val EVENT_ID_KEY = "event_id"
private const val FACEBOOK_ID_KEY = "facebook_id"

class FirebaseAnalyticsManager @Inject constructor(private val bundle: Bundle, private val firebaseAnalytics: FirebaseAnalytics) {

    fun logFacebookAttendEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_BUTTON, facebookId.let { putFacebookId(it) })
    }

    fun logFacebookAttendSuccessEvent(facebookId: String) {
        firebaseAnalytics.logEvent(EventTracking.Events.ATTEND_SUCCESS, putFacebookId(facebookId))
    }

    fun logShowEventDetailsEvent(eventId: Long) {
        firebaseAnalytics.logEvent(EventTracking.Events.SHOW_EVENT_DET, putEventId(eventId))
    }

    fun logMeetupPlaceEvent() {
        firebaseAnalytics.logEvent(EventTracking.Events.MEETUP_PLACE, null)
    }

    private fun putEventId(eventId: Long): Bundle {
        bundle.putLong(EVENT_ID_KEY, eventId)
        return bundle
    }

    private fun putFacebookId(facebookId: String): Bundle {
        bundle.putString(FACEBOOK_ID_KEY, facebookId)
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
    }

}