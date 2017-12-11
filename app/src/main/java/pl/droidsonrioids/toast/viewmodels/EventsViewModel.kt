package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonrioids.toast.data.Page
import pl.droidsonrioids.toast.data.State
import pl.droidsonrioids.toast.data.dto.EventDetailsDto
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.data.mapper.toViewModel
import pl.droidsonrioids.toast.data.wrapWithState
import pl.droidsonrioids.toast.repositories.EventsRepository
import pl.droidsonrioids.toast.utils.LoadingStatus
import pl.droidsonrioids.toast.utils.toPage
import javax.inject.Inject

class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel(), LoadingViewModel {

    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField()
    val isPreviousEventsEmpty = ObservableField<Boolean>(true)
    val upcomingEvent = ObservableField<UpcomingEventViewModel>()
    val previousEventsSubject: BehaviorSubject<List<State<EventItemViewModel>>> = BehaviorSubject.create()

    private var isPreviousEventsLoading: Boolean = false
    private var nextPageNumber: Int? = null
    private var eventsDisposable: Disposable? = null
    private val Any.simpleClassName:String get() = javaClass.simpleName


    init {
        loadEvents()
    }

    override fun retryLoading() {
        loadEvents()
    }

    private fun loadEvents() {
        loadingStatus.set(LoadingStatus.PENDING)
        eventsDisposable = eventsRepository.getEvents()
                .flatMap { (featuredEvent, previousEventsPage) ->
                    mapToSingleEventItemViewModelsPage(previousEventsPage)
                            .map { featuredEvent to it }
                            .toMaybe()
                }
                .subscribeBy(
                        onSuccess = (::onEventsLoaded),
                        onError = (::onEventsLoadError),
                        onComplete = (::onEmptyResponse)
                )
    }

    fun loadNextPage() {
        if (!isPreviousEventsLoading && nextPageNumber != null) {
            loadNextPage(nextPageNumber!!)
        }
    }

    private fun onEventsLoaded(events: Pair<EventDetailsDto, Page<State.Item<EventItemViewModel>>>) {
        val (upcomingEvent, previousEventsPage) = events
        this.upcomingEvent.set(UpcomingEventViewModel.create(upcomingEvent))
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
        eventsDisposable = eventsRepository.getEventsPage(pageNumber)
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
                        Log.d(simpleClassName, "Event item clicked $id")
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
        eventsDisposable?.dispose()
    }

}