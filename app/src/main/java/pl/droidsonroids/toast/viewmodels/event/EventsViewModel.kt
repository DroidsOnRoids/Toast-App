package pl.droidsonroids.toast.viewmodels.event

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.dto.event.EventDto
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.data.enums.ParentView
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.data.wrapWithState
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.repositories.facebook.FacebookRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.toPage
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class EventsViewModel @Inject constructor(
        loginStateWatcher: LoginStateWatcher,
        private val eventsRepository: EventsRepository,
        private val facebookRepository: FacebookRepository
) : ViewModel(), LoadingViewModel, NavigatingViewModel, LoginStateWatcher by loginStateWatcher {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField()
    val isPreviousEventsEmpty = ObservableField<Boolean>(true)
    val upcomingEvent = ObservableField<UpcomingEventViewModel>()
    val previousEventsSubject: BehaviorSubject<List<State<EventItemViewModel>>> = BehaviorSubject.create()
    val attendStatus: ObservableField<AttendStatus> = ObservableField(AttendStatus.DECLINED)

    private var isPreviousEventsLoading: Boolean = false
    private var nextPageNumber: Int? = null
    private var compositeDisposable = CompositeDisposable()
    private val Any.simpleClassName: String get() = javaClass.simpleName

    private var facebookId: String? = null

    private var facebookAttendStateDisposable: Disposable = Disposables.disposed()
    private var facebookAttendRequestDisposable: Disposable = Disposables.disposed()

    init {
        loadEvents()
        subscribeToLoginChange()
    }

    private fun subscribeToLoginChange() {
        compositeDisposable += loginStateSubject.subscribe {
            invalidateAttendState()
        }
    }

    fun invalidateAttendState() {
        facebookId?.let {
            facebookAttendStateDisposable.dispose()
            facebookAttendStateDisposable = facebookRepository.getEventAttendState(it)
                    .subscribeBy(
                            onSuccess = { status -> attendStatus.set(status) },
                            onError = {
                                navigationSubject.onNext(NavigationRequest.SnackBar(R.string.facebook_update_attend_error))
                                Log.e(simpleClassName, "Something went wrong with refreshing attend state", it)
                            }
                    )
        }
    }

    override fun retryLoading() {
        loadEvents()
    }

    private fun loadEvents() {
        loadingStatus.set(LoadingStatus.PENDING)
        compositeDisposable += eventsRepository.getEvents()
                .flatMap { (upcomingEvent, previousEventsPage) ->
                    updateFacebookAttend(upcomingEvent.facebookId)
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
                .subscribeBy(
                        onSuccess = (::onEventsLoaded),
                        onError = (::onEventsLoadError),
                        onComplete = (::onEmptyResponse)
                )
    }

    private fun updateFacebookAttend(facebookId: String) {
        this.facebookId = facebookId
        invalidateAttendState()
    }

    private fun onUpcomingEventLocationClick(coordinates: CoordinatesDto, placeName: String) {
        navigationSubject.onNext(NavigationRequest.Map(coordinates, placeName))
    }

    private fun onUpcomingEventClick(eventId: Long) {
        navigationSubject.onNext(NavigationRequest.EventDetails(eventId))
    }

    private fun onSeePhotosClick(eventId: Long, photos: List<ImageDto>) {
        navigationSubject.onNext(NavigationRequest.Photos(photos, eventId, ParentView.HOME))
    }

    private fun onAttendClick() {
        val attendStatus = this.attendStatus.get()
        when {
            !hasPermissions -> navigationSubject.onNext(NavigationRequest.LogIn)
            attendStatus == AttendStatus.DECLINED -> attendOnEvent()
            else -> {
                facebookId?.let {
                    navigationSubject.onNext(NavigationRequest.Website("https://www.facebook.com/events/$it"))
                }
            }
        }
    }

    private fun attendOnEvent() {
        facebookId?.let {
            facebookAttendRequestDisposable.dispose()
            facebookAttendRequestDisposable = facebookRepository.setEventAttending(it)
                    .subscribeBy(
                            onComplete = { attendStatus.set(AttendStatus.ATTENDING) },
                            onError = {
                                navigationSubject.onNext(NavigationRequest.SnackBar(R.string.oops_no_internet_connection))
                                Log.e(simpleClassName, "Something went wrong with attending to event", it)
                            }
                    )
        }
    }

    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isPreviousEventsLoading && nextPageNumber != null) {
            loadNextPage(nextPageNumber)
        }
    }

    private fun onEventsLoaded(events: Pair<UpcomingEventViewModel, Page<State.Item<EventItemViewModel>>>) {
        val (upcomingEventViewModel, previousEventsPage) = events
        upcomingEvent.set(upcomingEventViewModel)
        onPreviousEventsPageLoaded(previousEventsPage)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun onPreviousEventsPageLoaded(page: Page<State<EventItemViewModel>>) {
        val previousEvents = getPreviousEvents(page)
        isPreviousEventsEmpty.set(previousEvents.isEmpty())
        previousEventsSubject.onNext(previousEvents)
    }

    private fun getPreviousEvents(page: Page<State<EventItemViewModel>>): List<State<EventItemViewModel>> {
        var previousEvents = mergeWithExistingPreviousEvents(page.items)
        if (page.pageNumber < page.allPagesCount) {
            previousEvents += State.Loading
            nextPageNumber = page.pageNumber + 1
        } else {
            nextPageNumber = null
        }
        return previousEvents
    }

    private fun mergeWithExistingPreviousEvents(newList: List<State<EventItemViewModel>>): List<State<EventItemViewModel>> {
        val previousList = previousEventsSubject.value
                ?.filter { it is State.Item }
                ?: listOf()
        return previousList + newList
    }

    private fun onEventsLoadError(error: Throwable) {
        onEmptyResponse()
        Log.e(simpleClassName, "Something went wrong with fetching data for EventsViewModel", error)
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
                .map {
                    it.toViewModel { id ->
                        navigationSubject.onNext(NavigationRequest.EventDetails(id))
                    }
                }
                .map { wrapWithState(it) }
                .toPage(pageNo, pageCount)
    }

    private fun onPreviousEventsLoadError(throwable: Throwable) {
        Log.e(simpleClassName, "Something went wrong with fetching next previous events page for EventsViewModel", throwable)
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
        facebookAttendRequestDisposable.dispose()
        facebookAttendStateDisposable.dispose()
        compositeDisposable.dispose()
    }

}
