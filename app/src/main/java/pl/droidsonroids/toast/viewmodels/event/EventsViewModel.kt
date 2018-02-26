package pl.droidsonroids.toast.viewmodels.event

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.dto.event.EventDto
import pl.droidsonroids.toast.data.enums.ParentView
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.data.wrapWithState
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.SourceAttending
import pl.droidsonroids.toast.utils.toPage
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import pl.droidsonroids.toast.viewmodels.RefreshViewModel
import pl.droidsonroids.toast.viewmodels.facebook.AttendViewModel
import timber.log.Timber
import javax.inject.Inject

class EventsViewModel @Inject constructor(
        loginStateWatcher: LoginStateWatcher,
        attendViewModel: AttendViewModel,
        private val eventsRepository: EventsRepository,
        private val analyticsEventTracker: AnalyticsEventTracker
) : ViewModel(), LoadingViewModel, NavigatingViewModel, RefreshViewModel,
    LoginStateWatcher by loginStateWatcher, AttendViewModel by attendViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = navigationRequests

    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField()
    val isPreviousEventsEmpty = ObservableField<Boolean>(true)
    val upcomingEvent = ObservableField<UpcomingEventViewModel>()
    val previousEventsSubject: BehaviorSubject<List<State<EventItemViewModel>>> = BehaviorSubject.create()
    override val isSwipeRefreshLoaderVisibleSubject: PublishSubject<Boolean> = PublishSubject.create()

    private var isPreviousEventsLoading: Boolean = false
    private var nextPageNumber: Int? = null
    private var compositeDisposable = CompositeDisposable()


    init {
        loadEvents()
    }

    override fun retryLoading() {
        loadEvents()
    }

    private fun loadEvents() {
        loadingStatus.set(LoadingStatus.PENDING)
        compositeDisposable += getFirstPage()
                .subscribeBy(
                        onSuccess = (::onEventsLoaded),
                        onError = (::onEventsLoadError),
                        onComplete = (::onEmptyResponse)
                )
    }

    private fun getFirstPage(): Maybe<Pair<UpcomingEventViewModel, Page<State.Item<EventItemViewModel>>>> {
        return eventsRepository.getEvents()
                .flatMap { (upcomingEvent, previousEventsPage) ->
                    setEvent(upcomingEvent.facebookId, upcomingEvent.date, SourceAttending.UPCOMING_EVENT)
                    val upcomingEventViewModel = upcomingEvent.toViewModel(
                            onLocationClick = (::onUpcomingEventLocationClick),
                            onEventClick = (::onUpcomingEventClick),
                            onSeePhotosClick = (::onSeePhotosClick),
                            onAttendClick = (::onAttendClick)
                    )
                    mapToSingleEventItemViewModelsPage(previousEventsPage)
                            .map { upcomingEventViewModel to it }
                            .toMaybe()
                }
    }

    private fun onUpcomingEventLocationClick(coordinates: CoordinatesDto, placeName: String) {
        navigationSubject.onNext(NavigationRequest.Map(coordinates, placeName))
        analyticsEventTracker.logUpcomingEventTapMeetupPlaceEvent()
    }

    private fun onUpcomingEventClick(eventId: Long) {
        navigationSubject.onNext(NavigationRequest.EventDetails(eventId))
        analyticsEventTracker.logEventsShowEventDetailsEvent(eventId)
    }

    private fun onSeePhotosClick(eventId: Long, photos: List<ImageDto>) {
        navigationSubject.onNext(NavigationRequest.Photos(photos, eventId, ParentView.HOME))
    }

    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isPreviousEventsLoading && nextPageNumber != null) {
            loadNextPage(nextPageNumber)
        }
    }

    private fun onEventsLoaded(events: Pair<UpcomingEventViewModel, Page<State.Item<EventItemViewModel>>>) {
        onEventsRefreshed(events)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun onPreviousEventsPageLoaded(page: Page<State<EventItemViewModel>>) {
        val previousEvents = mergeWithExistingPreviousEvents(page.items)
                .addLoadingItemIfNeeded(page)
        setPreviousItems(previousEvents)
    }

    private fun setPreviousItems(previousEvents: List<State<EventItemViewModel>>) {
        isPreviousEventsEmpty.set(previousEvents.isEmpty())
        previousEventsSubject.onNext(previousEvents)
    }

    private fun List<State<EventItemViewModel>>.addLoadingItemIfNeeded(page: Page<State<EventItemViewModel>>): List<State<EventItemViewModel>> {
        return if (page.pageNumber < page.allPagesCount) {
            nextPageNumber = page.pageNumber + 1
            this + State.Loading
        } else {
            nextPageNumber = null
            this
        }
    }

    private fun mergeWithExistingPreviousEvents(newList: List<State<EventItemViewModel>>): List<State<EventItemViewModel>> {
        val previousList = previousEventsSubject.value
                ?.filter { it is State.Item }
                ?: emptyList()
        return previousList + newList
    }

    private fun onEventsLoadError(error: Throwable) {
        onEmptyResponse()
        Timber.e(error, "Something went wrong with fetching data for EventsViewModel")
    }

    private fun onEmptyResponse() {
        isPreviousEventsEmpty.set(true)
        loadingStatus.set(LoadingStatus.ERROR)
    }

    private fun loadNextPage(pageNumber: Int) {
        isPreviousEventsLoading = true
        compositeDisposable += eventsRepository.getEventsPage(pageNumber)
                .flatMap(::mapToSingleEventItemViewModelsPage)
                .doAfterSuccess { isPreviousEventsLoading = false }
                .subscribeBy(
                        onSuccess = (::onPreviousEventsPageLoaded),
                        onError = (::onPreviousEventsLoadError)
                )
    }

    private fun mapToSingleEventItemViewModelsPage(page: Page<EventDto>): Single<Page<State.Item<EventItemViewModel>>> {
        val (items, pageNo, pageCount) = page
        return items.toObservable()
                .map { it.toViewModel(::sendEventDetailsNavigationRequest) }
                .map { wrapWithState(it) }
                .toPage(pageNo, pageCount)
    }

    private fun sendEventDetailsNavigationRequest(id: Long) {
        navigationSubject.onNext(NavigationRequest.EventDetails(id))
        analyticsEventTracker.logEventsShowEventDetailsEvent(id)
    }

    private fun onPreviousEventsLoadError(throwable: Throwable) {
        Timber.e(throwable, "Something went wrong with fetching next previous events page for EventsViewModel")
        val previousEvents = mergeWithExistingPreviousEvents(listOf(createErrorState()))
        previousEventsSubject.onNext(previousEvents)
    }

    private fun createErrorState(): State.Error {
        return State.Error(::onErrorClick)
    }

    private fun onErrorClick() {
        val previousEvents = mergeWithExistingPreviousEvents(listOf(State.Loading))
        previousEventsSubject.onNext(previousEvents)
        nextPageNumber?.let { loadNextPage(it) }
    }

    override fun onCleared() {
        dispose()
        compositeDisposable.dispose()
    }

    override fun refresh() {
        invalidateAttendState()
        getFirstPage()
                .subscribeBy(
                        onSuccess = (::onEventsRefreshed),
                        onError = { onRefreshError() },
                        onComplete = (::onRefreshError)
                )
    }

    private fun onRefreshError() {
        navigationSubject.onNext(NavigationRequest.SnackBar(R.string.cannot_refresh_data))
        isSwipeRefreshLoaderVisibleSubject.onNext(false)
    }

    private fun onEventsRefreshed(events: Pair<UpcomingEventViewModel, Page<State.Item<EventItemViewModel>>>) {
        val (upcomingEventViewModel, previousEventsPage) = events
        upcomingEvent.set(upcomingEventViewModel)
        setPreviousItems(previousEventsPage.items.addLoadingItemIfNeeded(previousEventsPage))
        isSwipeRefreshLoaderVisibleSubject.onNext(false)
    }
}