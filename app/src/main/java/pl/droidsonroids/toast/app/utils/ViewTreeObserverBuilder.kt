package pl.droidsonroids.toast.app.utils

import android.view.View
import android.view.ViewTreeObserver


object ViewTreeObserverBuilder {
    fun build(animatedView: View, action: () -> Unit) {
        val viewTreeObserver = animatedView.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            addOnGlobalLayoutListener(viewTreeObserver, animatedView, action)
        }
    }

    private fun addOnGlobalLayoutListener(viewTreeObserver: ViewTreeObserver, animatedView: View, action: () -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                animatedView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                action()
            }
        })
    }
}