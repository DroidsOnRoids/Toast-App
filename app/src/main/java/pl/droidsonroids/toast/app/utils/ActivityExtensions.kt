@file:JvmName("ActivityExtensions")
package pl.droidsonroids.toast.app.utils

import android.app.Activity

fun Activity.turnOffActivityClosingAnimation() {
    overridePendingTransition(0, 0)
}
