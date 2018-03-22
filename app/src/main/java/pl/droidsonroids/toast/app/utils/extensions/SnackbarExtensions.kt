package pl.droidsonroids.toast.app.utils.extensions

import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.utils.NavigationRequest

fun Snackbar.setNavigationViewAnchor() =
        (view.layoutParams as CoordinatorLayout.LayoutParams).let {
            it.anchorId = R.id.homeNavigationView
            it.anchorGravity = Gravity.TOP
            it.gravity = Gravity.TOP
        }

fun View.showSnackbar(request: NavigationRequest.SnackBar, length: Int = Snackbar.LENGTH_SHORT, apply: Snackbar.() -> Unit = {}, onDismiss: () -> Unit = {}) =
        Snackbar.make(this, request.stringRes, length)
                .apply {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                }
                .apply { apply() }
                .addCallback(onDismiss = onDismiss)
                .show()

inline fun Snackbar.addCallback(crossinline onShow: () -> Unit = {}, crossinline onDismiss: () -> Unit = {}) = addCallback(
        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onShown(transientBottomBar: Snackbar?) = onShow()

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) = onDismiss()
        }
)