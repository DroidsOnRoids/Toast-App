package pl.droidsonroids.toast.app.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator


object RevealAnimationCreator {

    fun setVisibilityWithAnimation(animatedView: View, isVisible: Boolean, centerX: Int, centerY: Int) {
        val finalRadius = (Math.max(animatedView.width, animatedView.height)).toFloat()
        val revealAnimation: Animator
        if (isVisible) {
            revealAnimation = ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, 0f, finalRadius)
            animatedView.visibility = View.VISIBLE
        } else {
            revealAnimation = ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, finalRadius, 0f)
            revealAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animatedView.visibility = View.GONE
                }
            })
        }

        with(revealAnimation) {
            duration = 300
            interpolator = AccelerateInterpolator()
            start()
        }
    }
}