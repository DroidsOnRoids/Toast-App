package pl.droidsonroids.toast.app.behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.utils.extensions.hasSize
import kotlin.math.abs

private const val ACCELERATE_INTERPOLATOR_FACTOR = 0.5f
private const val DECELERATE_INTERPOLATOR_FACTOR = 2f
private const val UNKNOWN_VALUE = -1f

class SpeakerHeaderBehavior(private val context: Context, private val attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {
    private var childStartCenterXPosition = 0f
    private var childStartCenterYPosition = 0f
    private var childFinalXPosition = 0f
    private var childFinalHeight = 0f
    private var childFinalWidth = 0f
    private var toolbarCenterY = 0

    private val accelerateInterpolator = AccelerateInterpolator(ACCELERATE_INTERPOLATOR_FACTOR)

    private val decelerateInterpolator = DecelerateInterpolator(DECELERATE_INTERPOLATOR_FACTOR)

    private var initialised = false

    init {
        initAttributes()
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View) =
            dependency is AppBarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (child.hasSize) {
            child.initBehaviorProperties(dependency)
            val appBarClosingOffset = calculateToolbarClosingOffset(dependency as AppBarLayout)
            child.updateViewSize(appBarClosingOffset)
            child.updateViewPosition(appBarClosingOffset)
        }
        return true
    }

    private fun calculateToolbarClosingOffset(dependency: AppBarLayout): Float {
        return abs(dependency.y) / dependency.totalScrollRange
    }

    private fun View.updateViewPosition(appBarClosingOffset: Float) {
        val (translationX, translationY) = getTranslation(appBarClosingOffset)
        this.translationX = translationX
        this.translationY = translationY
    }

    private fun View.getTranslation(appBarClosingOffset: Float): Pair<Float, Float> {
        val translationX = -accelerateInterpolator.getInterpolation(appBarClosingOffset) * (childStartCenterXPosition - childFinalXPosition - childFinalWidth / 2)
        val translationY = when (id) {
            R.id.speakerName -> -appBarClosingOffset * (childStartCenterYPosition - toolbarCenterY)
            else -> -decelerateInterpolator.getInterpolation(appBarClosingOffset) * (childStartCenterYPosition - toolbarCenterY)
        }
        return translationX to translationY
    }

    private fun View.updateViewSize(appBarClosingOffset: Float) {
        val heightToSubtract = appBarClosingOffset * (height - childFinalHeight)
        val widthToSubtract = appBarClosingOffset * (width - childFinalWidth)

        scaleX = 1 - widthToSubtract / width
        scaleY = 1 - heightToSubtract / height
    }

    private fun initAttributes() {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SpeakerHeaderBehavior)
        childFinalHeight = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalHeight, UNKNOWN_VALUE)
        childFinalWidth = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalWidth, UNKNOWN_VALUE)
        childFinalXPosition = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalXPosition, 0f)
        attributes.recycle()
    }

    private fun View.initBehaviorProperties(dependency: View) {
        if (!initialised) {
            if (childFinalWidth == UNKNOWN_VALUE) childFinalWidth = width.toFloat()
            if (childFinalHeight == UNKNOWN_VALUE) childFinalHeight = height.toFloat()
            childStartCenterXPosition = left + width / 2f
            childStartCenterYPosition = top + height / 2f
            toolbarCenterY = dependency.findViewById<View>(R.id.toolbar).height / 2
            initialised = true
        }
    }
}