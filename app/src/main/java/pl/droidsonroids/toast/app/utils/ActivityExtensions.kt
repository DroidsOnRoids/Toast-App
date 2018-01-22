@file:JvmName("ActivityExtensions")
package pl.droidsonroids.toast.app.utils

import android.app.Activity

fun Activity.disableActivityTransitionAnimations() {
    overridePendingTransition(0, 0)
}
