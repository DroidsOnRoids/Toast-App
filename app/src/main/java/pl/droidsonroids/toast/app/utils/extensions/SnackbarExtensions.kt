package pl.droidsonroids.toast.app.utils.extensions

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.Gravity
import pl.droidsonroids.toast.R

fun Snackbar.setNavigationViewAnchor() = apply {
    (view.layoutParams as CoordinatorLayout.LayoutParams).let {
        it.anchorId = R.id.homeNavigationView
        it.anchorGravity = Gravity.TOP
        it.gravity = Gravity.TOP
    }
}