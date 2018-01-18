package pl.droidsonroids.toast.app.utils

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator


object RevealAnimatorBuilder {

    fun build(animatedView: View, centerX: Int, centerY: Int, isGrowing: Boolean): Animator? {
        val finalRadius = (Math.max(animatedView.width, animatedView.height)).toFloat()

        val revealAnimation =
                if (isGrowing) {
                    ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, 0f, finalRadius)
                } else {
                    ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, finalRadius, 0f)
                }

        with(revealAnimation) {
            duration = 300
            interpolator = AccelerateInterpolator()
        }

        return revealAnimation
    }
}