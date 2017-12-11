package pl.droidsonroids.toast.app.speakers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_speakers.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseDiffCallback
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.app.base.BaseStateAdapter
import pl.droidsonroids.toast.app.utils.LazyLoadingScrollListener
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.wrapWithState
import pl.droidsonroids.toast.viewmodels.SpeakerItemViewModel

class SpeakersFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_speakers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(speakersRecyclerView) {
            val speakersAdapter = SpeakersAdapter()
            adapter = speakersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addOnScrollListener(LazyLoadingScrollListener {

            })
            speakersAdapter.setData((0..10L).map { wrapWithState(SpeakerItemViewModel(it)) })
        }
    }
}

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
