package pl.droidsonrioids.toast.app.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonrioids.toast.R

class PreviousEventsAdapter : RecyclerView.Adapter<PreviousEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousEventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_loading_horizontal -> PreviousEventViewHolder.Loading(itemView)
            else -> PreviousEventViewHolder.Item(itemView)
        }
    }

    override fun onBindViewHolder(holder: PreviousEventViewHolder?, position: Int) {
        //TODO fill this with data after repository implemented
    }

    override fun getItemCount(): Int = 5 //TODO fill this with data after repository implemented

    override fun getItemViewType(position: Int): Int {
        // TODO add proper types for given position
        return when (position) {
            4 -> R.layout.item_loading_horizontal
            else -> R.layout.item_previous_event
        }
    }
}