package pl.droidsonroids.toast.app.events

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.databinding.ItemErrorHorizontalBinding

sealed class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class Item(private val itemBinding: ViewDataBinding) : StateViewHolder(itemBinding.root) {
        fun bind(state: State.Item<*>) {
            itemBinding.setVariable(BR.itemViewModel, state.item)
            itemBinding.executePendingBindings()
        }
    }

    class Loading(itemView: View) : StateViewHolder(itemView)

    class Error(private val errorBinding: ItemErrorHorizontalBinding) : StateViewHolder(errorBinding.root) {
        fun bind(errorState: State.Error) {
            errorBinding.errorState = errorState
        }
    }
}
