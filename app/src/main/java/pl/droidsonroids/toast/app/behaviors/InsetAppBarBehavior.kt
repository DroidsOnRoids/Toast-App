package pl.droidsonroids.toast.app.behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

class InsetAppBarBehavior(context: Context, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    private var appBarScrollOffset = 0

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        appBarScrollOffset = dependency.height + dependency.y.toInt()

        measure(child, parent.measuredWidth, parent.measuredHeight)
        layout(child)
        child.invalidate()
        return true
    }

    private fun measure(child: View, width: Int, height: Int) {
        child.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height - appBarScrollOffset, View.MeasureSpec.EXACTLY)
        )
    }

    override fun onMeasureChild(parent: CoordinatorLayout, child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        measure(
                child,
                View.MeasureSpec.getSize(parentWidthMeasureSpec),
                View.MeasureSpec.getSize(parentHeightMeasureSpec)
        )
        return true
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        layout(child)
        return true
    }

    private fun layout(child: View) {
        child.layout(0, appBarScrollOffset, child.measuredWidth, appBarScrollOffset + child.measuredHeight)
    }

}