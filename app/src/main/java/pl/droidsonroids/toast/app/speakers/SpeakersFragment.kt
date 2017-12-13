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
            addItemDecoration(SpeakerItemDecoration(context.applicationContext))
            // TODO: TOA-56 add lazy loading && data retrieving
            val sampleData = (0..10L)
                    .map { getSampleSpeaker(it) }
                    .map(::wrapWithState) + State.Loading + State.Error {}
            speakersAdapter.setData(sampleData)
        }
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

