@file:JvmName("ViewExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.view.View

val View.haveSize
    get() = width != 0 && height != 0

fun View.showWithFading() {
    animate().alpha(1f).withStartAction {
        visibility = View.VISIBLE
    }.start()
}

fun View.hideWithFading() {
    animate().alpha(0f).withEndAction {
        visibility = View.GONE
    }.start()
}