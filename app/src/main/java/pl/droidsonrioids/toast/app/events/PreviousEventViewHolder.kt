package pl.droidsonrioids.toast.app.events

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.State

sealed class PreviousEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class Item(private val itemBinding: ViewDataBinding) : PreviousEventViewHolder(itemBinding.root) {
        fun bind(state: State.Item<Event>) {
            itemBinding.setVariable(BR.viewModel, state.item)
            itemBinding.executePendingBindings()
        }
    }

    class Loading(itemView: View) : PreviousEventViewHolder(itemView)

    class Error(itemView: View) : PreviousEventViewHolder(itemView)
}
