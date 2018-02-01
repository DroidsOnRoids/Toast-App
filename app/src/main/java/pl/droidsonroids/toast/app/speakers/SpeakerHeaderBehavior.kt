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


class SpeakerHeaderBehavior(private val context: Context, private val attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {
    private var startXPositionImage = 0
    private var startYPositionImage = 0
    private var startHeight = 0
    private var startWidth = 0
    private var startToolbarHeight = 0

    private var initialised = false

    private var reduceToolbarHeight = 0f
    private var heightChildToReduce = 0f
    private var widthChildToReduce = 0f
    private var distanceX = 0f
    private var distanceY = 0f

    private var finalToolbarHeight = 0f
    private var finalXPosition = 0f
    private var finalYPosition = 0f
    private var finalHeight = 0f
    private var finalWidth = 0f

    init {
        initAttributes()
    }


    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View) =
            dependency is AppBarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return if (child.isNotEmpty()) {
            initProperties(child, dependency)

            val toolbarClosingProgress = calculateToolbarClosingProgressPercent(dependency)

            updateViewSize(toolbarClosingProgress, child)
            updateImagePosition(toolbarClosingProgress, child)
            true
        } else {
            false
        }
    }

    private fun calculateToolbarClosingProgressPercent(dependency: View): Float {
        var currentToolbarHeight = startToolbarHeight + dependency.y

        currentToolbarHeight = if (currentToolbarHeight < finalToolbarHeight) {
            finalToolbarHeight
        } else {
            currentToolbarHeight
        }

        val amountAlreadyMoved = startToolbarHeight - currentToolbarHeight
        return 100 * amountAlreadyMoved / reduceToolbarHeight
    }

    private fun updateImagePosition(progress: Float, child: View) {
        val distanceXToSubtract =
                if (child.id == R.id.speakerName) {
                    AccelerateInterpolator(12f).getInterpolation(progress / 100) * distanceX
                } else {
                    AccelerateInterpolator(0.5f).getInterpolation(progress / 100) * distanceX
                }
        val distanceYToSubtract =
                if (child.id == R.id.speakerName) {
                    AccelerateInterpolator(0.8f).getInterpolation(progress / 100) * distanceY
                } else {
                    DecelerateInterpolator(2f).getInterpolation(progress / 100) * distanceY
                }
        val newXPosition = startXPositionImage - distanceXToSubtract
        child.x = newXPosition
        child.y = startYPositionImage - distanceYToSubtract
    }

    private fun updateViewSize(progressPercent: Float, child: View) {
        val heightToSubtract = progressPercent / 100 * heightChildToReduce
        val widthToSubtract = progressPercent * widthChildToReduce / 100
        val layoutParams = child.layoutParams
        layoutParams.width = (startWidth - widthToSubtract).toInt()
        layoutParams.height = (startHeight - heightToSubtract).toInt()
        child.layoutParams = layoutParams
    }

    private fun initAttributes() {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SpeakerHeaderBehavior)
        finalXPosition = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalXPosition, 0f)
        finalYPosition = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalYPosition, 0f)
        finalHeight = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalHeight, 0f)
        finalWidth = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalWidth, 0f)
        finalToolbarHeight = attributes.getDimension(R.styleable.SpeakerHeaderBehavior_finalToolbarHeight, 0f)
        attributes.recycle()
    }

    private fun initProperties(child: View, dependency: View) {
        if (!initialised) {
            startHeight = child.height
            startWidth = child.width
            startXPositionImage = child.x.toInt()
            startYPositionImage = child.y.toInt()
            startToolbarHeight = dependency.height

            reduceToolbarHeight = startToolbarHeight - finalToolbarHeight
            heightChildToReduce = startHeight - finalHeight
            widthChildToReduce = startWidth - finalWidth
            distanceX = startXPositionImage - finalXPosition
            distanceY = startYPositionImage - finalYPosition
            initialised = true
        }
    }
}