package pl.droidsonroids.toast.app.utils

import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator


class RevealAnimationCreator {

    fun showAnimation(viewRoot: View, revealX: Int, revealY: Int) {
        with(viewRoot.viewTreeObserver) {
            if (isAlive) {
                addOnGlobalLayoutListener(this, viewRoot, revealX, revealY)
            }
        }
    }

    private fun addOnGlobalLayoutListener(viewTreeObserver: ViewTreeObserver, viewRoot: View, revealX: Int, revealY: Int) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                showRevealAnimation(viewRoot, revealX, revealY)
                viewRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun showRevealAnimation(viewRoot: View, rootX: Int, rootY: Int) {
        val finalRadius = (Math.max(viewRoot.width, viewRoot.height)).toFloat()
        val revealAnimation = ViewAnimationUtils.createCircularReveal(viewRoot, rootX, rootY, 0f, finalRadius)

        with(revealAnimation) {
            duration = 300
            interpolator = AccelerateInterpolator()
            start()
        }
    }
}