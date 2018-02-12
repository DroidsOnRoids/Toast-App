package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import pl.droidsonroids.toast.R

class SpeakerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val divider = ContextCompat.getDrawable(context, R.drawable.divider_speakers)

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom += divider?.intrinsicHeight ?: 0
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.itemAnimator?.isRunning != true) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            (0 until childCount - 1)
                    .map { parent.getChildAt(it) }
                    .forEach { it.drawDivider(left, right, canvas) }
        }
    }

    private fun View.drawDivider(left: Int, right: Int, canvas: Canvas) {
        divider?.let {
            val params = layoutParams as RecyclerView.LayoutParams
            val top = bottom + params.bottomMargin
            val bottom = top + it.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }

    }
}