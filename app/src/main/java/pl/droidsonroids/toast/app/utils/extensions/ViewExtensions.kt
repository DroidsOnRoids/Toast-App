@file:JvmName("ViewExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.view.View

fun View.isNotEmpty() =
        width != 0 && height != 0
