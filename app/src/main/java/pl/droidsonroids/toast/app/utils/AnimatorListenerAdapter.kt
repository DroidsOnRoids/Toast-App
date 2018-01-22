package pl.droidsonroids.toast.app.utils

import android.animation.Animator


interface AnimatorListenerAdapter : Animator.AnimatorListener {
    override fun onAnimationCancel(p0: Animator?) {}
    override fun onAnimationRepeat(p0: Animator?) {}
    override fun onAnimationStart(p0: Animator?) {}
}