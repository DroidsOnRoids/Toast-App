@file:JvmName("ActivityExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.app.Activity
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.behaviors.InsetAppBarBehavior

fun Activity.disableActivityTransitionAnimations() {
    overridePendingTransition(0, 0)
}

fun AppCompatActivity.addInsetAppBehaviorToLoadingView() {
    val loadingContainer = findViewById<FrameLayout>(R.id.loadingLayout)
    (loadingContainer.layoutParams as CoordinatorLayout.LayoutParams)
            .behavior = InsetAppBarBehavior(this)
}