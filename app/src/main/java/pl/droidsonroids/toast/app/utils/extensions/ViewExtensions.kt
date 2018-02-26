@file:JvmName("ViewExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.view.View

fun View.isNotEmpty() =
        width != 0 && height != 0

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}