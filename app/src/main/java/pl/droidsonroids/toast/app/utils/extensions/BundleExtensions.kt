@file:JvmName("BundleExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.os.Bundle
import pl.droidsonroids.toast.utils.EventTracking
import pl.droidsonroids.toast.utils.SortingType


fun Bundle.putGithub(link: String): Bundle {
    putString(EventTracking.Key.GITHUB_KEY, link)
    return this
}

fun Bundle.putEmail(link: String): Bundle {
    putString(EventTracking.Key.EMAIL_KEY, link)
    return this
}

fun Bundle.putWebsite(link: String): Bundle {
    putString(EventTracking.Key.WEBSITE_KEY, link)
    return this
}

fun Bundle.putTwitter(link: String): Bundle {
    putString(EventTracking.Key.TWITTER_KEY, link)
    return this
}

fun Bundle.putEventId(eventId: Long): Bundle {
    putLong(EventTracking.Key.EVENT_ID_KEY, eventId)
    return this
}

fun Bundle.putFacebookId(facebookId: String): Bundle {
    putString(EventTracking.Key.FACEBOOK_ID_KEY, facebookId)
    return this
}

fun Bundle.putSpeakerName(speakerName: String): Bundle {
    putString(EventTracking.Key.SPEAKER_ID_KEY, speakerName)
    return this
}

fun Bundle.putLectureName(lectureName: String): Bundle {
    putString(EventTracking.Key.LECTURE_NAME_KEY, lectureName)
    return this
}

fun Bundle.putSortingType(sortingType: SortingType): Bundle {
    putString(EventTracking.Key.SORTING_TYPE_KEY, when (sortingType) {
        SortingType.ALPHABETICAL -> EventTracking.Key.ALPHABETICAL_SORTING
        SortingType.DATE -> EventTracking.Key.DATE_SORTING
    })
    return this
}

fun Bundle.putTopicName(topicName: String): Bundle {
    putString(EventTracking.Key.TOPIC_NAME_KEY, topicName)
    return this
}

fun Bundle.putPhrase(phrase: String): Bundle {
    putString(EventTracking.Key.PHRASE_KEY, phrase)
    return this
}