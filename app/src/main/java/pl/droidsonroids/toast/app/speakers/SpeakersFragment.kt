package pl.droidsonroids.toast.app.speakers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_speakers.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.wrapWithState
import pl.droidsonroids.toast.utils.Constants.SEARCH_ITEM_ANIM_DURATION_MILLIS
import pl.droidsonroids.toast.utils.Constants.SEARCH_ITEM_HIDDEN_OFFSET
import pl.droidsonroids.toast.utils.Constants.SEARCH_ITEM_SHOWN_OFFSET
import pl.droidsonroids.toast.viewmodels.SpeakerItemViewModel

class SpeakersFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_speakers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showSearchMenuItemWithAnimation()
        setupRecyclerView()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            hideSearchMenuItemWithAnimation()
        } else {
            showSearchMenuItemWithAnimation()
        }
    }

    private fun showSearchMenuItemWithAnimation() {
        animateViewByY(SEARCH_ITEM_SHOWN_OFFSET)
    }

    private fun hideSearchMenuItemWithAnimation() {
        animateViewByY(SEARCH_ITEM_HIDDEN_OFFSET)
    }

    private fun animateViewByY(offset: Float) {
        activity?.run {
            findViewById<View>(R.id.menuItemSearch)
                    .animate()
                    .y(offset)
                    .setDuration(SEARCH_ITEM_ANIM_DURATION_MILLIS)
                    .start()
        }
    }

    private fun setupRecyclerView() {
        with(speakersRecyclerView) {
            val speakersAdapter = SpeakersAdapter()
            adapter = speakersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpeakerItemDecoration(context.applicationContext))

            // TODO: TOA-56 add lazy loading && data retrieving
            speakersAdapter.setData(getSampleData())
        }
    }

    private fun getSampleData(): List<State<SpeakerItemViewModel>> {
        return (0..10L)
                .map { getSampleSpeaker(it) }
                .map(::wrapWithState) + State.Loading + State.Error {}
    }

    private fun getSampleSpeaker(id: Long): SpeakerItemViewModel {
        // TODO: TOA-56  Change to real data
        return SpeakerItemViewModel(
                id,
                "John Doe",
                "Android Developer",
                ImageDto(
                        "http://api.letswift.pl/uploads/cache/efdebb744b2ca985de9d567eaa512c40.jpg",
                        "http://api.letswift.pl/uploads/cache/ba0e0f28e20aa16a657ea291b2eed477.jpg"
                )
        ) {
            Log.d(this::class.java.simpleName, "Clicked $it")
        }
    }
}