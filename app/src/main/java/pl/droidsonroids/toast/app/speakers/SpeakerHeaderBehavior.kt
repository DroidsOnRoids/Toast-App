package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.utils.extensions.isNotEmpty
import kotlin.math.abs

private const val ACCELERATE_INTERPOLATOR_FACTOR = 0.5f
private const val DECELERATE_INTERPOLATOR_FACTOR = 2f
private const val UNKNOWN_VALUE = -1f
private const val LOG_TAG = "SPEAKER_BEHAVIOR"

class SpeakerHeaderBehavior(private val context: Context, private val attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {
    private var childStartCenterXPosition = 0f
    private var childStartCenterYPosition = 0f
    private var childStartHeight = 0f
    private var childStartWidth = 0f
    private var appBarStartHeight = 0f

    private var finalToolbarHeight = 0f
    private var childFinalXPosition = 0f
    private var childFinalYPosition = 0f
    private var childFinalHeight = 0f
    private var childFinalWidth = 0f

    private var appBarHeightToReduce = 0f
    private var childHeightToReduce = UNKNOWN_VALUE
    private var childWidthToReduce = UNKNOWN_VALUE
    private var childTotalDistanceX = 0f
    private var childTotalDistanceY = 0f

    private val accelerateInterpolator = AccelerateInterpolator(ACCELERATE_INTERPOLATOR_FACTOR)
    private val decelerateInterpolator = DecelerateInterpolator(DECELERATE_INTERPOLATOR_FACTOR)

    private var initialised = false

    init {
        initAttributes()
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View) =
            dependency is AppBarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (child.isNotEmpty()) {
            child.post {
                initProperties(child, dependency)
                val appBarClosingOffset = calculateToolbarClosingOffset(dependency as AppBarLayout)

                updateViewSize(appBarClosingOffset, child)
                updateViewPosition(appBarClosingOffset, child)
                child.requestLayout()
            }
        }
        return true
    }

    private fun calculateToolbarClosingOffset(dependency: AppBarLayout): Float {
        return abs(dependency.y) / appBarHeightToReduce
    }

    private fun updateViewPosition(appBarClosingOffset: Float, child: View) {
        val (distanceXToSubtract, distanceYToSubtract) = getDistanceToSubtract(child, appBarClosingOffset)
        child.x = childStartCenterXPosition - distanceXToSubtract
        child.y = childStartCenterYPosition - distanceYToSubtract
    }

    private fun getDistanceToSubtract(child: View, appBarClosingOffset: Float): Pair<Float, Float> =
            when (child.id) {
                R.id.speakerName -> {
                    val distanceXToSubtract = accelerateInterpolator.getInterpolation(appBarClosingOffset) * childTotalDistanceX + child.width / 2
                    val distanceYToSubtract = appBarClosingOffset * childTotalDistanceY + child.height / 2
                    Pair(distanceXToSubtract, distanceYToSubtract)
                }
                else -> {
                    val distanceXToSubtract = accelerateInterpolator.getInterpolation(appBarClosingOffset) * childTotalDistanceX + child.width / 2
                    val distanceYToSubtract = decelerateInterpolator.getInterpolation(appBarClosingOffset) * childTotalDistanceY + child.height / 2
                    Pair(distanceXToSubtract, distanceYToSubtract)
                }
            }

    private fun updateViewSize(appBarClosingOffset: Float, child: View) {
        val heightToSubtract = appBarClosingOffset * childHeightToReduce
        val widthToSubtract = appBarClosingOffset * childWidthToReduce

        val offsetHeight = abs(heightToSubtract / childStartHeight - 1)
        val offsetWidth = abs(widthToSubtract / childStartWidth - 1)

        child.scaleX = offsetWidth
        child.scaleY = offsetHeight
    }

    private fun initAttributes() {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SpeakerHeaderBehavior)
        finalToolbarHeight = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalToolbarHeight, 0f)
        childFinalHeight = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalHeight, -1f)
        childFinalWidth = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalWidth, -1f)
        childFinalXPosition = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalXPosition, 0f)
        childFinalYPosition = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalYPosition, 0f)
        attributes.recycle()
    }

    private fun initProperties(child: View, dependency: View) {
        if (!initialised) {
            childStartHeight = child.height.toFloat()
            childStartWidth = child.width.toFloat()
            childStartCenterXPosition = (child.x.toInt() + child.width / 2).toFloat()
            childStartCenterYPosition = (child.y.toInt() + child.height / 2).toFloat()
            appBarStartHeight = dependency.height.toFloat()

            if (childFinalWidth == UNKNOWN_VALUE) childFinalWidth = childStartWidth
            if (childFinalHeight == UNKNOWN_VALUE) childFinalHeight = childStartHeight

            appBarHeightToReduce = appBarStartHeight - finalToolbarHeight
            childHeightToReduce = childStartHeight - childFinalHeight
            childWidthToReduce = childStartWidth - childFinalWidth
            childTotalDistanceX = childStartCenterXPosition - childFinalXPosition - childFinalWidth / 2
            childTotalDistanceY = childStartCenterYPosition - childFinalYPosition - childFinalHeight / 2


            initialised = true
        }
    }
}