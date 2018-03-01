@file:JvmName("IntExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.content.res.Resources


val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()