package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import pl.droidsonroids.toast.R


class SpeakerHeaderBehavior : CoordinatorLayout.Behavior<ImageView> {
    private var startXPositionImage: Int = 0
    private var startYPositionImage: Int = 0
    private var startHeight: Int = 0
    private var startToolbarHeight: Int = 0

    private var initialised = false

    private var amountOfToolbarToMove: Float = 0.toFloat()
    private var amountOfImageToReduce: Float = 0.toFloat()
    private var amountToMoveXPosition: Float = 0.toFloat()
    private var amountToMoveYPosition: Float = 0.toFloat()

    // user configured params
    private var finalToolbarHeight: Float = 0.toFloat()
    private var finalXPosition: Float = 0.toFloat()
    private var finalYPosition: Float = 0.toFloat()
    private var finalHeight: Float = 0.toFloat()

    constructor(
            context: Context,
            attrs: AttributeSet?) {

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SpeakerHeaderBehavior)
            finalXPosition = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalXPosition, 0f)
            finalYPosition = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalYPosition, 0f)
            finalHeight = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalHeight, 0f)
            finalToolbarHeight = a.getDimension(R.styleable.SpeakerHeaderBehavior_finalToolbarHeight, 0f)
            a.recycle()
        }
    }

    override fun layoutDependsOn(
            parent: CoordinatorLayout,
            child: ImageView,
            dependency: View): Boolean {

        return dependency is AppBarLayout // change if you want another sibling to depend on
    }

    override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: ImageView,
            dependency: View): Boolean {

        // make child (avatar) change in relation to dependency (toolbar) in both size and position, init with properties from layout
        initProperties(child, dependency)

        // calculate progress of movement of dependency
        var currentToolbarHeight = startToolbarHeight + dependency.y // current expanded height of toolbar
        // don't go below configured min height for calculations (it does go passed the toolbar)
        currentToolbarHeight = if (currentToolbarHeight < finalToolbarHeight) finalToolbarHeight else currentToolbarHeight
        val amountAlreadyMoved = startToolbarHeight - currentToolbarHeight
        val progress = 100 * amountAlreadyMoved / amountOfToolbarToMove // how much % of expand we reached

        // update image size
        val heightToSubtract = progress * amountOfImageToReduce / 100
        val lp = child.getLayoutParams() as CoordinatorLayout.LayoutParams
        lp.width = (startHeight - heightToSubtract).toInt()
        lp.height = (startHeight - heightToSubtract).toInt()
        child.setLayoutParams(lp)

        // update image position
        val distanceXToSubtract = progress * amountToMoveXPosition / 100
        val distanceYToSubtract = progress * amountToMoveYPosition / 100
        val newXPosition = startXPositionImage - distanceXToSubtract
        //newXPosition = newXPosition < endXPosition ? endXPosition : newXPosition; // don't go passed end position
        child.setX(newXPosition)
        child.setY(startYPositionImage - distanceYToSubtract)

        return true
    }

    private fun initProperties(
            child: ImageView,
            dependency: View) {

        if (!initialised) {
            // form initial layout
            startHeight = child.getHeight()
            startXPositionImage = child.getX().toInt()
            startYPositionImage = child.getY().toInt()
            startToolbarHeight = dependency.height
            // some calculated fields
            amountOfToolbarToMove = startToolbarHeight - finalToolbarHeight
            amountOfImageToReduce = startHeight - finalHeight
            amountToMoveXPosition = startXPositionImage - finalXPosition
            amountToMoveYPosition = startYPositionImage - finalYPosition
            initialised = true
        }
    }
}