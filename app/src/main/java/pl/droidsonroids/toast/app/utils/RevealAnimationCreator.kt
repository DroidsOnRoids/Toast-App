package pl.droidsonroids.toast.app.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator


class RevealAnimationCreator(private val isGrowing: Boolean) {

    fun showAnimation(animatedView: View, centerX: Int, centerY: Int) {
        with(animatedView.viewTreeObserver) {
            if (isAlive) {
                addOnGlobalLayoutListener(this, animatedView, centerX, centerY)
            }
        }
    }

    private fun addOnGlobalLayoutListener(viewTreeObserver: ViewTreeObserver, animatedView: View, centerX: Int, centerY: Int) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                showRevealAnimation(animatedView, centerX, centerY)
                animatedView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun showRevealAnimation(animatedView: View, centerX: Int, centerY: Int) {
        val finalRadius = (Math.max(animatedView.width, animatedView.height)).toFloat()
        val revealAnimation: Animator
        if (isGrowing) {
            revealAnimation = ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, 0f, finalRadius)
        } else {
            revealAnimation = ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, finalRadius, 0f)
            revealAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animatedView.visibility = View.GONE
                }
            })
        }

        with(revealAnimation) {
            animatedView.visibility = View.VISIBLE
            duration = 300
            interpolator = AccelerateInterpolator()
            start()
        }
    }
}