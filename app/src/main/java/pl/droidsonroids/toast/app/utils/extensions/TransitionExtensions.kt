package pl.droidsonroids.toast.app.utils.extensions

import android.transition.Transition

fun Transition.doOnEnd(onEndCallback: () -> Unit): Transition {
    return addListener(object : Transition.TransitionListener {
        override fun onTransitionEnd(transition: Transition) {
            onEndCallback()
            transition.removeListener(this)
        }

        override fun onTransitionResume(transition: Transition?) = Unit

        override fun onTransitionPause(transition: Transition?) = Unit

        override fun onTransitionCancel(transition: Transition?) = Unit

        override fun onTransitionStart(transition: Transition?) = Unit
    })
}