package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import pl.droidsonroids.toast.R


class SpeakerHeaderBehavior(context: Context, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>() {
    private var startXPositionImage = 0
    private var startYPositionImage = 0
    private var startHeight = 0
    private var startWidth = 0
    private var startToolbarHeight = 0

    private var initialised = false

    private var amountOfToolbarToMove = 0f
    private var amountOfImageToReduceHeight = 0f
    private var amountOfImageToReduceWidth = 0f
    private var amountToMoveXPosition = 0f
    private var amountToMoveYPosition = 0f

    private var finalToolbarHeight = 0f
    private var finalXPosition = 0f
    private var finalYPosition = 0f
    private var finalHeight = 0f
    private var finalWidth = 0f

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SpeakerHeaderBehavior)
        finalXPosition = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalXPosition, 0f)
        finalYPosition = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalYPosition, 0f)
        finalHeight = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalHeight, 0f)
        finalWidth = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalWidth, 0f)
        finalToolbarHeight = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalToolbarHeight, 0f)
        a.recycle()
    }


    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View) =
            dependency is AppBarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        initProperties(child, dependency)

        val progress = calculateMovementProgress(dependency)

        updateViewSize(progress, child)
        updateImagePosition(progress, child)

        return true
    }

    private fun calculateMovementProgress(dependency: View): Float {
        var currentToolbarHeight = startToolbarHeight + dependency.y

        currentToolbarHeight = if (currentToolbarHeight < finalToolbarHeight) {
            finalToolbarHeight
        } else {
            currentToolbarHeight
        }

        val amountAlreadyMoved = startToolbarHeight - currentToolbarHeight
        return 100 * amountAlreadyMoved / amountOfToolbarToMove
    }

    private fun updateImagePosition(progress: Float, child: View) {
        val distanceXToSubtract = progress * amountToMoveXPosition / 100
        val distanceYToSubtract = progress * amountToMoveYPosition / 100
        val newXPosition = startXPositionImage - distanceXToSubtract
        child.x = newXPosition
        child.y = startYPositionImage - distanceYToSubtract
    }

    private fun updateViewSize(progress: Float, child: View) {
        val heightToSubtract = progress * amountOfImageToReduceHeight / 100
        val widthToSubtract = progress * amountOfImageToReduceWidth / 100
        val layoutParams = child.layoutParams
        layoutParams.width = (startWidth - widthToSubtract).toInt()
        layoutParams.height = (startHeight - heightToSubtract).toInt()
        child.layoutParams = layoutParams
    }

    private fun initProperties(child: View, dependency: View) {

        if (!initialised) {
            startHeight = child.height
            startWidth = child.width
            startXPositionImage = child.x.toInt()
            startYPositionImage = child.y.toInt()
            startToolbarHeight = dependency.height

            amountOfToolbarToMove = startToolbarHeight - finalToolbarHeight
            amountOfImageToReduceHeight = startHeight - finalHeight
            amountOfImageToReduceWidth = startWidth - finalWidth
            amountToMoveXPosition = startXPositionImage - finalXPosition
            amountToMoveYPosition = startYPositionImage - finalYPosition
            initialised = true
        }
    }
}