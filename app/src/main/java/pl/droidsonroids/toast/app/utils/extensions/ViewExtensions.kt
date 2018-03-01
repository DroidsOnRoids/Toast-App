@file:JvmName("ViewExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.view.View

val View.haveSize
    get() = width != 0 && height != 0
