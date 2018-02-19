package pl.droidsonroids.toast.app.behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

class InsetAppBarBehavior(private val context: Context, private val attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }


    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val appBarHeight = dependency.height + dependency.y.toInt()
        child.run {
            setPadding(paddingLeft, appBarHeight, paddingRight, paddingBottom)
        }
        return true
    }
}