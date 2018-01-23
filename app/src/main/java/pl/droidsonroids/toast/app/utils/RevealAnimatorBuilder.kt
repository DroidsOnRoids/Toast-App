package pl.droidsonroids.toast.app.utils

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator


object RevealAnimatorBuilder {
    private const val ANIM_DURATION_MILLIS = 300L

    fun build(animatedView: View, centerX: Int, centerY: Int, isGrowing: Boolean): Animator {
        val finalRadius = (Math.max(animatedView.width, animatedView.height)).toFloat()
        val revealAnimation = createRevealAnimation(isGrowing, animatedView, centerX, centerY, finalRadius)
        revealAnimation.duration = ANIM_DURATION_MILLIS
        revealAnimation.interpolator = AccelerateInterpolator()

        return revealAnimation
    }

    private fun createRevealAnimation(isGrowing: Boolean, animatedView: View, centerX: Int, centerY: Int, finalRadius: Float): Animator {
        return if (isGrowing) {
            ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, 0f, finalRadius)
        } else {
            ViewAnimationUtils.createCircularReveal(animatedView, centerX, centerY, finalRadius, 0f)
        }
    }
}