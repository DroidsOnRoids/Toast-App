package pl.droidsonrioids.toast.app.events

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.State
import pl.droidsonrioids.toast.viewmodels.EventItemViewModel

class PreviousEventsAdapter : RecyclerView.Adapter<PreviousEventViewHolder>() {

    private var data: List<State<EventItemViewModel>> = listOf()

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
            is PreviousEventViewHolder.Item -> holder.bind(data[position] as State.Item<EventItemViewModel>)
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

    fun setData(newData: List<State<EventItemViewModel>>) {
        val diff = DiffUtil.calculateDiff(EventItemDiffCallback(data, newData))
        data = newData
        diff.dispatchUpdatesTo(this)
    }

    class EventItemDiffCallback(private val old: List<State<EventItemViewModel>>, private val new: List<State<EventItemViewModel>>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]
            return when {
                oldItem is State.Item && newItem is State.Item -> oldItem.item.id == newItem.item.id
                oldItem is State.Loading && newItem is State.Loading -> true
                oldItem is State.Error && newItem is State.Error -> true
                else -> false
            }
        }

        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]
            return when {
                oldItem is State.Item && newItem is State.Item -> oldItem.item == newItem.item
                oldItem is State.Loading && newItem is State.Loading -> true
                oldItem is State.Error && newItem is State.Error -> oldItem.action == newItem.action
                else -> false
            }
        }
    }
}