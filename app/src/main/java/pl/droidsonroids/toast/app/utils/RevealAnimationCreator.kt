package pl.droidsonroids.toast.app.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator


class RevealAnimationCreator(private val isGrowing: Boolean) {

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
        val revealAnimation: Animator
        if (isGrowing) {
            revealAnimation = ViewAnimationUtils.createCircularReveal(viewRoot, rootX, rootY, 0f, finalRadius)
        } else {
            revealAnimation = ViewAnimationUtils.createCircularReveal(viewRoot, rootX, rootY, finalRadius, 0f)
            revealAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    viewRoot.visibility = View.GONE
                }
            })
        }

        with(revealAnimation) {
            viewRoot.visibility = View.VISIBLE
            duration = 300
            interpolator = AccelerateInterpolator()
            start()
        }
    }
}