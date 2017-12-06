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

class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {


    val featuredEvent = ObservableField<UpcomingEventViewModel>()
    val previousEventsSubject: BehaviorSubject<List<State<EventItemViewModel>>> = BehaviorSubject.create()
    private var eventsDisposable: Disposable? = null
    private var nextPageNumber: Int? = null

    private var isPreviousEventsLoading: Boolean = false
    var loadingStatus: ObservableField<LoadingStatus> = ObservableField()
        private set
    // TODO:  TOA-42 Add previous events handling
    var lastEvents: List<EventDto> = emptyList()
        private set

    init {
        loadEvents()
    }

    fun loadEvents() {
        loadingStatus.set(LoadingStatus.PENDING)
        uploadEventsFromApi()
    }

    private fun uploadEventsFromApi() {

        eventsDisposable = eventsRepository.getEvents()
                .flatMap { (featuredEvent, previousEventsPage) ->
                    mapToSingleEventItemViewModelsPage(previousEventsPage)
                            .map { featuredEvent to it }
                            .toMaybe()
                }
                .subscribeBy(
                        onSuccess = (::onEventsLoaded),
                        onError = {
                            loadingStatus.set(LoadingStatus.ERROR)
                            Log.e(this::class.java.simpleName, "Something went wrong with fetching data for EventsViewModel", it)
                        }
                )
    }

    private fun onEventsLoaded(events: Pair<EventDetailsDto, Page<State.Item<EventItemViewModel>>>) {
        val (featuredEvent, previousEventsPage) = events
        this.featuredEvent.set(UpcomingEventViewModel.create(featuredEvent))
        onPreviousEventsPageLoaded(previousEventsPage)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun onPreviousEventsPageLoaded(page: Page<State<EventItemViewModel>>) {
        var previousEvents = mergeWithExistingPreviousEvents(page.items)
        if (page.pageNumber < page.allPagesCount) {
            previousEvents += State.Loading
            nextPageNumber = page.pageNumber + 1
        } else {
            nextPageNumber = null
        }
        previousEventsSubject.onNext(previousEvents)
    }

    private fun mergeWithExistingPreviousEvents(newList: List<State<EventItemViewModel>>): List<State<EventItemViewModel>> {
        val previousList = previousEventsSubject.value
                ?.filter { it is State.Item }
                ?: listOf()
        return previousList + newList
    }

    fun loadNextPage() {
        nextPageNumber?.takeIf { !isPreviousEventsLoading }
                ?.let {
                    loadNextPage(it)
                }
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
                        Log.d(this::class.java.simpleName, "Event item clicked $id")
                    }
                }
                .map { wrapWithState(it) }
                .toPage(pageNo, pageCount)
    }

    private fun onPreviousEventsLoadError(throwable: Throwable) {
        Log.e(this::class.java.simpleName, "Something went wrong with fetching next previous events page for EventsViewModel", throwable)
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