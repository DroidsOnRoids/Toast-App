package pl.droidsonroids.toast.app.utils.extensions

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.utils.NavigationRequest

fun Snackbar.setNavigationViewAnchor() = apply {
    (view.layoutParams as CoordinatorLayout.LayoutParams).let {
        it.anchorId = R.id.homeNavigationView
        it.anchorGravity = Gravity.TOP
        it.gravity = Gravity.TOP
    }
}

fun View.showSnackbar(request: NavigationRequest.SnackBar, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, request.stringRes, length)
            .apply {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
            .setNavigationViewAnchor()
            .show()
}