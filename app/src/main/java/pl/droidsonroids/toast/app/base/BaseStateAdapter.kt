package pl.droidsonroids.toast.app.base

import android.databinding.DataBindingUtil
import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.events.StateViewHolder
import pl.droidsonroids.toast.data.State

abstract class BaseStateAdapter<in T>(private val isHorizontal: Boolean) : RecyclerView.Adapter<StateViewHolder>() {
    private var data: List<State<T>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_loading_horizontal,
            R.layout.item_loading_vertical -> StateViewHolder.Loading(inflater.inflate(viewType, parent, false))
            R.layout.item_error_horizontal,
            R.layout.item_error_vertical -> StateViewHolder.Error(DataBindingUtil.inflate(inflater, viewType, parent, false))
            else -> StateViewHolder.Item(DataBindingUtil.inflate(inflater, viewType, parent, false))
        }
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        when (holder) {
            is StateViewHolder.Item -> holder.bind(data[position] as State.Item<*>)
            is StateViewHolder.Error -> holder.bind(data[position] as State.Error)
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is State.Loading -> if (isHorizontal) R.layout.item_loading_horizontal else R.layout.item_loading_vertical
            is State.Error -> if (isHorizontal) R.layout.item_error_horizontal else R.layout.item_error_vertical
            else -> getLayoutForPosition(position)
        }
    }

    fun setData(newData: List<State<T>>) {
        val diff = DiffUtil.calculateDiff(getDiffCallback(data, newData))
        data = newData
        diff.dispatchUpdatesTo(this)
    }

    abstract fun getDiffCallback(oldList: List<State<T>>, newList: List<State<T>>): BaseDiffCallback<T>

    @LayoutRes
    abstract fun getLayoutForPosition(position: Int): Int

}