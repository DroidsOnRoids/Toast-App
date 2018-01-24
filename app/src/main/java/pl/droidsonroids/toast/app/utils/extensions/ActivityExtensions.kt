@file:JvmName("ActivityExtensions")
package pl.droidsonroids.toast.app.utils.extensions

import android.app.Activity

fun Activity.disableActivityTransitionAnimations() {
    overridePendingTransition(0, 0)
}
