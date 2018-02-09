@file:JvmName("BundleExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.os.Bundle


private const val EVENT_ID = "event_id"
private const val FACEBOOK_ID = "facebook_id"

fun Bundle.putEventId(eventId: Long): Bundle {
    putLong(EVENT_ID, eventId)
    return this
}

fun Bundle.putFacebookId(facebookId: String): Bundle {
    putString(FACEBOOK_ID, facebookId)
    return this
}