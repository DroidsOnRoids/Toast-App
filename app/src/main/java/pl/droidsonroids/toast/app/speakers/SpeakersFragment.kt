package pl.droidsonroids.toast.app.speakers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_events.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.app.events.PreviousEventsAdapter
import pl.droidsonroids.toast.app.utils.LazyLoadingScrollListener

class SpeakersFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_speakers, container, false)

    private fun setupRecyclerView() {
        with(previousEventsRecyclerView) {
            val previousEventsAdapter = PreviousEventsAdapter()
            adapter = previousEventsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addOnScrollListener(LazyLoadingScrollListener {

            })
        }
    }
}