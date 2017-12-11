package pl.droidsonroids.toast.app.base

import android.support.v7.util.DiffUtil
import pl.droidsonroids.toast.data.State

abstract class BaseDiffCallback<in T>(private val oldEventsList: List<State<T>>, private val newEventsList: List<State<T>>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldEventsList[oldItemPosition]
        val newItem = newEventsList[newItemPosition]
        return when {
            oldItem is State.Item && newItem is State.Item -> areStateItemsTheSame(oldItem, newItem)
            oldItem is State.Loading && newItem is State.Loading -> true
            oldItem is State.Error && newItem is State.Error -> true
            else -> false
        }
    }

    override fun getOldListSize() = oldEventsList.size

    override fun getNewListSize() = newEventsList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldEventsList[oldItemPosition]
        val newItem = newEventsList[newItemPosition]
        return when {
            oldItem is State.Item && newItem is State.Item -> areStateItemsContentTheSame(oldItem, newItem)
            oldItem is State.Loading && newItem is State.Loading -> true
            oldItem is State.Error && newItem is State.Error -> oldItem.action == newItem.action
            else -> false
        }
    }

    abstract fun areStateItemsTheSame(oldItem: State.Item<T>, newItem: State.Item<T>): Boolean

    abstract fun areStateItemsContentTheSame(oldItem: State.Item<T>, newItem: State.Item<T>): Boolean
}