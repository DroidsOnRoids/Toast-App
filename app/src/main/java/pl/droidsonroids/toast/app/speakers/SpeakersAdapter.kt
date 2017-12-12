package pl.droidsonroids.toast.app.speakers

import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseDiffCallback
import pl.droidsonroids.toast.app.base.BaseStateAdapter
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.viewmodels.SpeakerItemViewModel

class SpeakersAdapter : BaseStateAdapter<SpeakerItemViewModel>(false) {
    override fun getDiffCallback(oldList: List<State<SpeakerItemViewModel>>, newList: List<State<SpeakerItemViewModel>>): BaseDiffCallback<SpeakerItemViewModel> {
        return SpeakerItemDiffCallback(oldList, newList)
    }

    override fun getLayoutForPosition(position: Int) = R.layout.item_speaker

    class SpeakerItemDiffCallback(oldList: List<State<SpeakerItemViewModel>>, newList: List<State<SpeakerItemViewModel>>)
        : BaseDiffCallback<SpeakerItemViewModel>(oldList, newList) {

        override fun areStateItemsTheSame(oldItem: State.Item<SpeakerItemViewModel>, newItem: State.Item<SpeakerItemViewModel>): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areStateItemsContentTheSame(oldItem: State.Item<SpeakerItemViewModel>, newItem: State.Item<SpeakerItemViewModel>): Boolean {
            return oldItem.item == newItem.item
        }

    }

}