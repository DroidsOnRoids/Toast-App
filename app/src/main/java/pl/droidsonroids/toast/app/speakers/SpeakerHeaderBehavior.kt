package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.utils.extensions.isNotEmpty
import kotlin.math.abs

private const val ACCELERATE_INTERPOLATOR_FACTOR = 0.5f
private const val DECELERATE_INTERPOLATOR_FACTOR = 2f

class SpeakerHeaderBehavior(private val context: Context, private val attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {
    private var childStartCenterXPosition = 0
    private var childStartCenterYPosition = 0
    private var childStartHeight = 0
    private var childStartWidth = 0
    private var childStartAppBarHeight = 0

    private var finalToolbarHeight = 0f
    private var childFinalCenterXPosition = 0f
    private var childFinalCenterYPosition = 0f
    private var childFinalHeight = 0f
    private var childFinalWidth = 0f

    private var appBarHeightToReduce = 0f
    private var childHeightToReduce = 0f
    private var childWidthToReduce = 0f
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
                Log.wtf("SPEAKER_BEHAVIOR", "Offset: $appBarClosingOffset")
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
                    val distanceXToSubtract = decelerateInterpolator.getInterpolation(appBarClosingOffset) * childTotalDistanceX + child.width / 2
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
        val layoutParams = child.layoutParams
        layoutParams.width = (childStartWidth - widthToSubtract).toInt()
        layoutParams.height = (childStartHeight - heightToSubtract).toInt()
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
            childStartAppBarHeight = dependency.height

            appBarHeightToReduce = childStartAppBarHeight - finalToolbarHeight
            childHeightToReduce = childStartHeight - childFinalHeight
            childWidthToReduce = childStartWidth - childFinalWidth
            childTotalDistanceX = childStartCenterXPosition - childFinalCenterXPosition
            childTotalDistanceY = childStartCenterYPosition - childFinalCenterYPosition

            initialised = true
        }
    }
}