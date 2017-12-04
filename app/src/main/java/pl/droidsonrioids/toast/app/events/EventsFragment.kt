package pl.droidsonrioids.toast.app.events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.fragment_events.*
import pl.droidsonrioids.toast.R

const val TOP_BAR_TRANSLATION_FACTOR = 2f

class EventsFragment : Fragment() {

    private val maxToolbarElevation by lazy {
        resources.getDimensionPixelSize(R.dimen.home_toolbar_elevation).toFloat()
    }
    private val topBarHeight by lazy {
        resources.getDimensionPixelSize(R.dimen.events_top_bar_height).toFloat()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_events, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupAppBarShadow()
    }

    private fun setupRecyclerView() {
        with(previousEventsRecyclerView) {
            adapter = PreviousEventsAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper = HorizontalSnapHelper(layoutManager)
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun setupAppBarShadow() {
        val shadowInterpolator = DecelerateInterpolator(TOP_BAR_TRANSLATION_FACTOR)

        eventsScrollContainer.setOnScrollChangeListener { _: NestedScrollView?, _, scrollY, _, _ ->
            topBar.translationY = -scrollY * TOP_BAR_TRANSLATION_FACTOR

            val shadow = scrollY.takeIf { it < topBarHeight }
                    ?.let { shadowInterpolator.getInterpolation(it / topBarHeight) * maxToolbarElevation }
                    ?: maxToolbarElevation

            shadowCreator.translationZ = shadow
        }
    }

}