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
import pl.droidsonrioids.toast.utils.toPage
import javax.inject.Inject


class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {


    val featuredEvent = ObservableField<UpcomingEventViewModel>()
    val previousEventsSubject: BehaviorSubject<List<State<EventItemViewModel>>> = BehaviorSubject.create()
    private var eventsDisposable: Disposable? = null
    private var nextPageNo: Int? = null

    private var isPreviousEventsLoading: Boolean = false

    init {
        eventsDisposable = eventsRepository.getEvents()
                .flatMap { (featuredEvent, previousEventsPage) ->
                    mapToSingleEventItemViewModelsPage(previousEventsPage)
                            .map { featuredEvent to it }
                            .toMaybe()
                }
                .subscribeBy(
                        onSuccess = (::onEventsLoaded),
                        onError = {
                            Log.e(this::class.java.simpleName, "Something went wrong with fetching data for EventsViewModel", it)
                        }
                )
    }

    private fun onEventsLoaded(events: Pair<EventDetailsDto, Page<State.Item<EventItemViewModel>>>) {
        val (featuredEvent, previousEventsPage) = events
        this.featuredEvent.set(UpcomingEventViewModel.create(featuredEvent))
        onPreviousEventsPageLoaded(previousEventsPage)
    }

    private fun onPreviousEventsPageLoaded(page: Page<State<EventItemViewModel>>) {
        var previousEvents = mergeWithExistingPreviousEvents(page.items)
        if (page.pageNo < page.pageCount) {
            previousEvents += State.Loading
            nextPageNo = page.pageNo + 1
        } else {
            nextPageNo = null
        }
        previousEventsSubject.onNext(previousEvents)
    }

    private fun mergeWithExistingPreviousEvents(newList: List<State<EventItemViewModel>>): List<State<EventItemViewModel>> {
        val previousList = previousEventsSubject.value
                ?.filter { it is State.Item }
                ?: listOf()
        return previousList + newList
    }

    fun loadNextPage(force: Boolean = false) {
        nextPageNo?.takeIf { !isPreviousEventsLoading || force }
                ?.let {
                    isPreviousEventsLoading = true
                    eventsDisposable = eventsRepository.getEventsPage(it)
                            .flatMap(::mapToSingleEventItemViewModelsPage)
                            .doAfterSuccess { isPreviousEventsLoading = false }
                            .subscribeBy(
                                    onSuccess = (::onPreviousEventsPageLoaded),
                                    onError = (::onPreviousEventsLoadError)
                            )
                }
    }

    private fun onPreviousEventsLoadError(throwable: Throwable) {
        Log.e(this::class.java.simpleName, "Something went wrong with fetching next previous events page for EventsViewModel", throwable)
        val previousEvents = mergeWithExistingPreviousEvents(listOf(State.Error {
            val previousEvents = mergeWithExistingPreviousEvents(listOf(State.Loading))
            previousEventsSubject.onNext(previousEvents)
            loadNextPage(true)
        }))
        previousEventsSubject.onNext(previousEvents)
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

    override fun onCleared() {
        eventsDisposable?.dispose()
    }

}