package pl.droidsonrioids.toast.app.events

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.State

class PreviousEventsAdapter : RecyclerView.Adapter<PreviousEventViewHolder>() {

    private var data: List<State<Event>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_loading_horizontal -> PreviousEventViewHolder.Loading(inflater.inflate(viewType, parent, false))
            R.layout.item_error_horizontal -> PreviousEventViewHolder.Error(inflater.inflate(viewType, parent, false))
            else -> PreviousEventViewHolder.Item(DataBindingUtil.inflate(inflater, viewType, parent, false))
        }
    }

    override fun onBindViewHolder(holder: PreviousEventViewHolder, position: Int) {
        when (holder) {
            is PreviousEventViewHolder.Item -> holder.bind(data[position] as State.Item<Event>)
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is State.Loading -> R.layout.item_loading_horizontal
            is State.Error -> R.layout.item_error_horizontal
            else -> R.layout.item_previous_event
        }
    }

    fun setData(list: List<State<Event>>) {
        data = list
        notifyDataSetChanged()
    }
}