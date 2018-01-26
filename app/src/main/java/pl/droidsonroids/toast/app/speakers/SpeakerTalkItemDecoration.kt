package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import pl.droidsonroids.toast.R

class SpeakerTalkItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val margin = context.resources.getDimensionPixelSize(R.dimen.margin_small)

    override fun getItemOffsets(outRect: Rect, child: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, child, parent, state)
        // TODO: 26/01/2018 Fix snap & decoration size
        if (parent.getChildAdapterPosition(child) != 0) {
            outRect.left += margin
        }
    }
}