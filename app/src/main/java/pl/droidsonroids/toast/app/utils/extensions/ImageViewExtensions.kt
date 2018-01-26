@file:JvmName("ImageViewExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.widget.ImageView

fun ImageView.setImageColor(colorId: Int) {
    this.setColorFilter(ContextCompat.getColor(this.context, colorId), PorterDuff.Mode.SRC_IN)
}

