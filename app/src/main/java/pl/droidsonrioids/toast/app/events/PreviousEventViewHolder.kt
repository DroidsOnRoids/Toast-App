package pl.droidsonrioids.toast.app.events

import android.support.v7.widget.RecyclerView
import android.view.View

sealed class PreviousEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class Item(itemView: View) : PreviousEventViewHolder(itemView) {

    }

    class Loading(itemView: View) : PreviousEventViewHolder(itemView)

    class Error(itemView: View) : PreviousEventViewHolder(itemView)
}
