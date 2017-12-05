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
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.data.wrapWithState
import pl.droidsonrioids.toast.repositories.EventsRepository
import pl.droidsonrioids.toast.utils.toPage
import javax.inject.Inject


class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {


    val featuredEvent = ObservableField<UpcomingEventViewModel>()
    val previousEvents: BehaviorSubject<List<State<EventItemViewModel>>> = BehaviorSubject.create()
    private var eventsDisposable: Disposable? = null
    private var nextPageNo: Int? = null

    init {
        eventsDisposable = eventsRepository.getEvents()
                .flatMap { (featuredEvent, previousEventsPage) ->
                    mapToSingleEventItemViewModelsPage(previousEventsPage)
                            .map { featuredEvent to it }
                            .toMaybe()
                }
                .doAfterTerminate { eventsDisposable?.dispose() }
                .subscribeBy(
                        onSuccess = { (featuredEvent, previousEventsPage) ->
                            this.featuredEvent.set(UpcomingEventViewModel.create(featuredEvent))
                            handleNewEventsPage(previousEventsPage)
                        },
                        onError = {
                            Log.e(this::class.java.simpleName, "Something went wrong with fetching data for EventsViewModel", it)
                        }
                )
    }

    private fun handleNewEventsPage(page: Page<State<EventItemViewModel>>) {
        var newList = mergeWithExistingList(page.items)
        if (page.pageNo < page.pageCount) {
            newList += State.Loading
            nextPageNo = page.pageNo + 1
        } else {
            nextPageNo = null
        }
        previousEvents.onNext(newList)
    }

    private fun mergeWithExistingList(newList: List<State<EventItemViewModel>>): List<State<EventItemViewModel>> {
        val previousList = previousEvents.value
                ?.filter { it is State.Item }
                ?: listOf()
        return previousList + newList
    }

    fun loadNextPage() {
        nextPageNo?.takeIf { eventsDisposable?.isDisposed == true }
                ?.let {
                    eventsDisposable = eventsRepository.getEventsPage(it)
                            .flatMap(this::mapToSingleEventItemViewModelsPage)
                            .doAfterTerminate { eventsDisposable?.dispose() }
                            .subscribeBy(
                                    onSuccess = (this::handleNewEventsPage),
                                    onError = {
                                        Log.e(this::class.java.simpleName, "Something went wrong with fetching next previous events page for EventsViewModel", it)
                                    }
                            )
                }
    }

    private fun mapToSingleEventItemViewModelsPage(page: Page<EventDto>): Single<Page<State.Item<EventItemViewModel>>> {
        val (items, pageNo, pageCount) = page
        return items.toObservable()
                .map { it.toViewModel() }
                .map { wrapWithState(it) }
                .toPage(pageNo, pageCount)
    }

    private fun EventDto.toViewModel(): EventItemViewModel {
        return EventItemViewModel(
                id,
                title,
                date,
                coverImages.firstOrNull()
        ) {
            Log.d(this::class.java.simpleName, "Event item clicked $id")
        }
    }

    override fun onCleared() {
        eventsDisposable?.dispose()
    }

}