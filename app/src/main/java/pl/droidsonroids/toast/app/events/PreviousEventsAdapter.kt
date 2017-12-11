package pl.droidsonroids.toast.app.events

import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseDiffCallback
import pl.droidsonroids.toast.app.base.BaseStateAdapter
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.viewmodels.EventItemViewModel

class PreviousEventsAdapter : BaseStateAdapter<EventItemViewModel>(true) {
    override fun getDiffCallback(oldList: List<State<EventItemViewModel>>, newList: List<State<EventItemViewModel>>): BaseDiffCallback<EventItemViewModel> {
        return EventItemDiffCallback(oldList, newList)
    }

    override fun getLayoutForPosition(position: Int) = R.layout.item_previous_event


    class EventItemDiffCallback(oldEventsList: List<State<EventItemViewModel>>, newEventsList: List<State<EventItemViewModel>>)
        : BaseDiffCallback<EventItemViewModel>(oldEventsList, newEventsList) {

        override fun areStateItemsTheSame(oldItem: State.Item<EventItemViewModel>, newItem: State.Item<EventItemViewModel>): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areStateItemsContentTheSame(oldItem: State.Item<EventItemViewModel>, newItem: State.Item<EventItemViewModel>): Boolean {
            return oldItem.item == newItem.item
        }
    }
}