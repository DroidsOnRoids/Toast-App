package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_speakers.*
import kotlinx.android.synthetic.main.layout_speakers_sorting_bar.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.app.utils.callbacks.LazyLoadingScrollListener
import pl.droidsonroids.toast.app.utils.extensions.showSnackbar
import pl.droidsonroids.toast.databinding.FragmentSpeakersBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.speaker.SpeakersViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val STRAIGHT_ANGLE = 180f
private const val MIN_CLICK_COUNT = 5

class SpeakersFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    private lateinit var speakersViewModel: SpeakersViewModel

    private var navigationDisposable: Disposable = Disposables.disposed()

    private var compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentSpeakersBinding.inflate(inflater, container, false)
        binding.speakersViewModel = speakersViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showSearchMenuItemWithAnimation()
        setupRecyclerView()
        showSearchMenuItemWithAnimation()
        setupSwipeRefresh()
        subscribeToSortingDetailsVisibilityChange()
    }

    private fun setupViewModel() {
        speakersViewModel = ViewModelProviders.of(this, viewModelFactory)[SpeakersViewModel::class.java]
        navigationDisposable = speakersViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
    }

    private fun handleNavigationRequest(request: NavigationRequest) {
        when (request) {
            is NavigationRequest.SnackBar -> speakersSwipeRefresh.showSnackbar(request)
            is NavigationRequest.SpeakerDetails -> {
                navigator.showActivityWithSharedAnimation(activity as MainActivity, request, getSharedViews(request.id))
            }
            else -> navigator.dispatch(baseActivity, request)
        }
    }

    private fun getSharedViews(speakerId: Long): Array<Pair<View, String>> {
        return speakersRecyclerView.findViewHolderForItemId(speakerId)
                ?.itemView
                ?.run {
                    val speakerAvatar = findViewById<View>(R.id.speakerAvatarImage)
                    arrayOf(Pair(speakerAvatar, speakerAvatar.transitionName))
                } ?: emptyArray()
    }

    private fun setupSwipeRefresh() {
        speakersSwipeRefresh.setOnRefreshListener(speakersViewModel::refresh)
        compositeDisposable += speakersViewModel.isSwipeRefreshLoaderVisibleSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speakersSwipeRefresh::setRefreshing)
    }

    private fun showSearchMenuItemWithAnimation() {
        animateViewByY(Constants.SearchMenuItem.SHOWN_OFFSET)
    }

    private fun hideSearchMenuItemWithAnimation() {
        animateViewByY(Constants.SearchMenuItem.HIDDEN_OFFSET)
    }

    private fun animateViewByY(offset: Float) {
        (activity as MainActivity).animateSearchButton(offset)
    }

    private fun subscribeToSortingDetailsVisibilityChange() {
        compositeDisposable += speakersViewModel.isSortingDetailsVisible
                .subscribe(::animateSortingDetails)

        compositeDisposable += speakersViewModel.isSortingDetailsVisible
                .skip(1L)
                .buffer(speakersViewModel.isSortingDetailsVisible.debounce(1, TimeUnit.SECONDS))
                .filter { it.size >= MIN_CLICK_COUNT }
                .subscribeWithMakorAnimation()
    }

    private fun animateSortingDetails(it: Boolean) {
        sortingDetailsLayout.animate()
                .translationY(if (it) sortingDetailsLayout.height.toFloat() else 0f)
                .start()
        arrowDownImage.animate()
                .rotation(if (it) STRAIGHT_ANGLE else 0f)
                .start()
    }

    private fun Observable<List<Boolean>>.subscribeWithMakorAnimation(): Disposable {
        return observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showMakor() }
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { hideMakor() }
    }

    private fun hideMakor() {
        makor.animate().translationY(0f).start()
    }

    private fun showMakor() {
        makor.animate().translationY(makor.height.toFloat()).start()
    }

    private fun setupRecyclerView() {
        with(speakersRecyclerView) {
            val speakersAdapter = SpeakersAdapter()
            adapter = speakersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpeakerItemDecoration(context.applicationContext))
            addOnScrollListener(LazyLoadingScrollListener {
                speakersViewModel.loadNextPage()
            })

            subscribeToSpeakersChange(speakersAdapter)
        }
    }

    private fun subscribeToSpeakersChange(speakersAdapter: SpeakersAdapter) {
        compositeDisposable += speakersViewModel.speakersSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speakersAdapter::setData)
    }

    override fun onDestroyView() {
        hideSearchMenuItemWithAnimation()
        compositeDisposable.clear()
        super.onDestroyView()
    }

    override fun onDetach() {
        navigationDisposable.dispose()
        compositeDisposable.dispose()
        super.onDetach()
    }
}