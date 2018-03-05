package pl.droidsonroids.toast.app.behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import pl.droidsonroids.toast.utils.consume

class InsetAppBarBehavior(context: Context, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {

    private var appBarScrollOffset = 0

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View) = consume {
        appBarScrollOffset = dependency.height + dependency.y.toInt()

        measure(child, parent.measuredWidth, parent.measuredHeight)
        layout(child)
        child.invalidate()
    }

    private fun measure(child: View, width: Int, height: Int) {
        child.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height - appBarScrollOffset, View.MeasureSpec.EXACTLY)
        )
    }

    override fun onMeasureChild(parent: CoordinatorLayout, child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int) = consume {
        measure(
                child,
                View.MeasureSpec.getSize(parentWidthMeasureSpec),
                View.MeasureSpec.getSize(parentHeightMeasureSpec)
        )
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int) = consume {
        layout(child)
    }

    private fun layout(child: View) {
        child.layout(0, appBarScrollOffset, child.measuredWidth, appBarScrollOffset + child.measuredHeight)
    }

}