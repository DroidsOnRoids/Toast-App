package pl.droidsonroids.toast.app.utils

import android.animation.Animator


interface AnimatorEndListener : Animator.AnimatorListener {
    override fun onAnimationCancel(p0: Animator?) {}
    override fun onAnimationRepeat(p0: Animator?) {}
    override fun onAnimationStart(p0: Animator?) {}
}