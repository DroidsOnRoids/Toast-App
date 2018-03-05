package pl.droidsonroids.toast.app.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.layout_card_upcoming_event.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.app.utils.callbacks.LazyLoadingScrollListener
import pl.droidsonroids.toast.app.utils.extensions.showSnackbar
import pl.droidsonroids.toast.databinding.FragmentEventsBinding
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.event.EventsViewModel
import java.util.*
import javax.inject.Inject

private const val TOP_BAR_TRANSLATION_FACTOR = 2f

class EventsFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    private lateinit var eventsViewModel: EventsViewModel

    private val compositeDisposable = CompositeDisposable()

    private var navigationDisposable: Disposable = Disposables.disposed()

    private val snackbarQueue = LinkedList<NavigationRequest.SnackBar>()

    private val topBarHeight by lazy {
        resources.getDimension(R.dimen.events_top_bar_height)
    }

    private val maxToolbarElevation by lazy {
        resources.getDimension(R.dimen.home_toolbar_elevation)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel()
    }

    private fun setupViewModel() {
        eventsViewModel = ViewModelProviders.of(this, viewModelFactory)[EventsViewModel::class.java]
        navigationDisposable = eventsViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
    }

    private fun handleNavigationRequest(request: NavigationRequest) {
        when (request) {
            is NavigationRequest.SnackBar -> showSnackbar(request)
            is NavigationRequest.EventDetails -> showEventDetails(request)
            else -> activity?.let { navigator.dispatch(it, request) }
        }
    }

    private fun showSnackbar(request: NavigationRequest.SnackBar) {
        snackbarQueue.add(request)
        if (snackbarQueue.size == 1) {
            showNextSnackbar()
        }
    }

    private fun showEventDetails(request: NavigationRequest.EventDetails) {
        navigator.showActivityWithSharedAnimation(activity as AppCompatActivity, request, getSharedViews(request.id))
    }

    private fun getSharedViews(id: Long): Array<Pair<View, String>> {
        return if (eventsViewModel.isUpcomingEvent(id)) {
            arrayOf(Pair(upcomingEventImage as View, upcomingEventImage.transitionName))
        } else {
            getSharedViewFromRecyclerView(id)
        }
    }

    private fun getSharedViewFromRecyclerView(id: Long): Array<Pair<View, String>> {
        return previousEventsRecyclerView.findViewHolderForItemId(id)
                ?.itemView
                ?.run {
                    val coverImage = findViewById<View>(R.id.eventCoverImage)
                    arrayOf(Pair(coverImage, coverImage.transitionName))
                } ?: emptyArray()
    }

    private fun showNextSnackbar() {
        val snackbarRequest = snackbarQueue.peek()
        if (isVisible && snackbarRequest != null) {
            eventsScrollContainer.showSnackbar(snackbarRequest, onDismiss = {
                snackbarQueue.poll()
                showNextSnackbar()
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)
        binding.eventsViewModel = eventsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupAppBarShadow()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        eventsSwipeRefresh.setOnRefreshListener(eventsViewModel::refresh)
        compositeDisposable += eventsViewModel.isSwipeRefreshLoaderVisibleSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventsSwipeRefresh::setRefreshing)
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
        compositeDisposable += eventsViewModel.previousEventsSubject
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

    override fun onStart() {
        super.onStart()
        eventsViewModel.invalidateAttendState()
    }

    override fun onDestroyView() {
        snackbarQueue.clear()
        compositeDisposable.clear()
        super.onDestroyView()
    }

    override fun onDetach() {
        compositeDisposable.dispose()
        navigationDisposable.dispose()
        super.onDetach()
    }
}

