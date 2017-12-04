package pl.droidsonrioids.toast.app.events

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

class HorizontalSnapHelper(layoutManager: RecyclerView.LayoutManager) : LinearSnapHelper() {
    private val horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray {
        val horizontalDistance = horizontalHelper.getDecoratedStart(targetView) - horizontalHelper.startAfterPadding
        return intArrayOf(horizontalDistance, 0)
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return if (layoutManager is LinearLayoutManager) {
            findView(layoutManager)
        } else {
            super.findSnapView(layoutManager)
        }
    }

    private fun findView(linearLayoutManager: LinearLayoutManager): View? {
        with(linearLayoutManager) {
            val firstVisibleItemPosition = findFirstVisibleItemPosition()
            val noVisibleItems = firstVisibleItemPosition == RecyclerView.NO_POSITION
            val isEndOfList = findLastCompletelyVisibleItemPosition() == itemCount - 1
            if (noVisibleItems || isEndOfList) {
                return null
            }
            val firstVisibleView = findViewByPosition(firstVisibleItemPosition)
            val viewVisibleWidth = horizontalHelper.getDecoratedEnd(firstVisibleView).toFloat()
            val viewWidth = horizontalHelper.getDecoratedMeasurement(firstVisibleView).toFloat()
            val visibilityPercent = viewVisibleWidth / viewWidth
            val firstCompletelyVisibleItemPosition = findFirstCompletelyVisibleItemPosition()
            val hasFirstCompletelyVisibleItem = firstCompletelyVisibleItemPosition != RecyclerView.NO_POSITION
            return when {
                visibilityPercent > 0.5 -> firstVisibleView
                hasFirstCompletelyVisibleItem ->
                    findViewByPosition(firstCompletelyVisibleItemPosition)
                else -> findViewByPosition(firstVisibleItemPosition + 1)
            }
        }
    }
}