package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.utils.extensions.isNotEmpty


class SpeakerHeaderBehavior(private val context: Context, private val attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {
    private var childStartCenterXPosition = 0
    private var childStartCenterYPosition = 0
    private var childStartHeight = 0
    private var childStartWidth = 0
    private var childStartToolbarHeight = 0

    private var finalToolbarHeight = 0f
    private var childFinalCenterXPosition = 0f
    private var childFinalCenterYPosition = 0f
    private var childFinalHeight = 0f
    private var childFinalWidth = 0f

    private var toolbarHeightToReduce = 0f
    private var childHeightToReduce = 0f
    private var childWidthToReduce = 0f
    private var childTotalDistanceX = 0f
    private var childTotalDistanceY = 0f

    private var initialised = false

    init {
        initAttributes()
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View) =
            dependency is AppBarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (child.isNotEmpty()) {
            initProperties(child, dependency)
            val toolbarClosingOffset = calculateToolbarClosingOffset(dependency)

            updateViewSize(toolbarClosingOffset, child)
            updateViewPosition(toolbarClosingOffset, child)
        }
        return true
    }

    private fun calculateToolbarClosingOffset(dependency: View): Float {
        val currentToolbarHeight = childStartToolbarHeight + dependency.y
        val amountAlreadyMoved = childStartToolbarHeight - currentToolbarHeight
        return amountAlreadyMoved / toolbarHeightToReduce
    }

    private fun updateViewPosition(toolbarClosingOffset: Float, child: View) {
        val (distanceXToSubtract, distanceYToSubtract) = getDistanceToSubtract(child, toolbarClosingOffset)
        child.x = childStartCenterXPosition - distanceXToSubtract
        child.y = childStartCenterYPosition - distanceYToSubtract
    }

    private fun getDistanceToSubtract(child: View, toolbarClosingOffset: Float): Pair<Float, Float> {
        val distanceXToSubtract: Float
        val distanceYToSubtract: Float

        when (child.id) {
            R.id.speakerName -> {
                distanceXToSubtract = OvershootInterpolator(0f).getInterpolation(toolbarClosingOffset) * childTotalDistanceX + child.width / 2
                distanceYToSubtract = toolbarClosingOffset * childTotalDistanceY + child.height / 2
            }
            else -> {
                distanceXToSubtract = AccelerateInterpolator(0.5f).getInterpolation(toolbarClosingOffset) * childTotalDistanceX + child.width / 2
                distanceYToSubtract = DecelerateInterpolator(2f).getInterpolation(toolbarClosingOffset) * childTotalDistanceY + child.height / 2
            }
        }
        return Pair(distanceXToSubtract, distanceYToSubtract)
    }

    private fun updateViewSize(toolbarClosingOffset: Float, child: View) {
        val heightToSubtract = toolbarClosingOffset * childHeightToReduce
        val widthToSubtract = toolbarClosingOffset * childWidthToReduce
        val layoutParams = child.layoutParams
        layoutParams.width = (childStartWidth - widthToSubtract).toInt()
        layoutParams.height = (childStartHeight - heightToSubtract).toInt()
        child.layoutParams = layoutParams
    }

    private fun initAttributes() {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SpeakerHeaderBehavior)
        finalToolbarHeight = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalToolbarHeight, 0f)
        childFinalHeight = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalHeight, 0f)
        childFinalWidth = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalWidth, 0f)
        childFinalCenterXPosition = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalXPosition, 0f) + childFinalWidth / 2
        childFinalCenterYPosition = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalYPosition, 0f) + childFinalHeight / 2
        attributes.recycle()
    }

    private fun initProperties(child: View, dependency: View) {
        if (!initialised) {
            childStartHeight = child.height
            childStartWidth = child.width
            childStartCenterXPosition = child.x.toInt() + child.width / 2
            childStartCenterYPosition = child.y.toInt() + child.height / 2
            childStartToolbarHeight = dependency.height

            toolbarHeightToReduce = childStartToolbarHeight - finalToolbarHeight
            childHeightToReduce = childStartHeight - childFinalHeight
            childWidthToReduce = childStartWidth - childFinalWidth
            childTotalDistanceX = childStartCenterXPosition - childFinalCenterXPosition
            childTotalDistanceY = childStartCenterYPosition - childFinalCenterYPosition

            initialised = true
        }
    }
}