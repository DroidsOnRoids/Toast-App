package pl.droidsonrioids.toast.app.events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_events.*
import pl.droidsonrioids.toast.R

class EventsFragment : Fragment() {

    private var isShadowHidden: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_events, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupAppBarShadow()
    }

    private fun setupRecyclerView() {
        with(previousEventsList) {
            adapter = PreviousEventsAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupAppBarShadow() {
        eventsScrollContainer.setOnScrollChangeListener { _: NestedScrollView?, _, scrollY, _, _ ->
            val state = scrollY == 0
            if (isShadowHidden != state) {
                isShadowHidden = state
                if (state) {
                    shadowCreator.animate().translationZ(0f).start()
                } else
                    shadowCreator.animate().translationZ(resources.getDimensionPixelSize(R.dimen.home_toolbar_shadow).toFloat()).start()
            }
        }
    }

}