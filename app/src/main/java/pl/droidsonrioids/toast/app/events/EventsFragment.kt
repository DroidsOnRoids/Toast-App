package pl.droidsonrioids.toast.app.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_events.*
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.app.base.BaseFragment
import pl.droidsonrioids.toast.app.utils.LazyLoadingScrollListener
import pl.droidsonrioids.toast.databinding.FragmentEventsBinding
import pl.droidsonrioids.toast.viewmodels.EventsViewModel

private const val TOP_BAR_TRANSLATION_FACTOR = 2f

class EventsFragment : BaseFragment() {

    private lateinit var eventsViewModel: EventsViewModel
    private var previousEventsDisposable: Disposable? = null

    private val topBarHeight by lazy {
        resources.getDimension(R.dimen.events_top_bar_height)
    }

    private val maxToolbarElevation by lazy {
        resources.getDimension(R.dimen.home_toolbar_elevation)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventsViewModel = ViewModelProviders.of(this, viewModelFactory)[EventsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)
        binding.eventsViewModel = eventsViewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupAppBarShadow()
    }

    private fun setupRecyclerView() {
        with(previousEventsRecyclerView) {
            val previousEventsAdapter = PreviousEventsAdapter()
            adapter = previousEventsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper = HorizontalSnapHelper(layoutManager)
            snapHelper.attachToRecyclerView(this)
            addOnScrollListener(LazyLoadingScrollListener {
                eventsViewModel.loadNextPage()
            })
            subscribeToPreviousEventChange(previousEventsAdapter)
        }
    }

    private fun subscribeToPreviousEventChange(previousEventsAdapter: PreviousEventsAdapter) {
        previousEventsDisposable = eventsViewModel.previousEventsSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(previousEventsAdapter::setData)
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

    override fun onDestroyView() {
        previousEventsDisposable?.dispose()
        super.onDestroyView()
    }
}

