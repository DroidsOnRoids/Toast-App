package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.Page
import pl.droidsonrioids.toast.data.model.State
import pl.droidsonrioids.toast.data.model.wrapWithState
import pl.droidsonrioids.toast.repositories.EventsRepository
import pl.droidsonrioids.toast.utils.toPage
import javax.inject.Inject


class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {


    val featuredEvent = ObservableField<UpcomingEventViewModel>()
    val previousEvents: BehaviorSubject<List<State<EventItemViewModel>>> = BehaviorSubject.create()
    private var disposable: Disposable? = null
    private var nextPageNo: Int? = null

    init {
        disposable = eventsRepository.getEvents()
                .flatMap { (featuredEvent, previousEventsPage) ->
                    mapToSingleEventItemViewModelsPage(previousEventsPage)
                            .map { featuredEvent to it }
                            .toMaybe()
                }
                .doAfterTerminate { disposable?.dispose() }
                .subscribeBy(
                        onSuccess = { (featuredEvent, previousEventsPage) ->
                            this.featuredEvent.set(UpcomingEventViewModel(featuredEvent))
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
        nextPageNo?.takeIf { disposable?.isDisposed == true }
                ?.let {
                    disposable = eventsRepository.getEventsPage(it)
                            .flatMap(this::mapToSingleEventItemViewModelsPage)
                            .doAfterTerminate { disposable?.dispose() }
                            .subscribeBy(
                                    onSuccess = (this::handleNewEventsPage),
                                    onError = {
                                        Log.e(this::class.java.simpleName, "Something went wrong with fetching next previous events page for EventsViewModel", it)
                                    }
                            )
                }
    }

    private fun mapToSingleEventItemViewModelsPage(page: Page<Event>): Single<Page<State.Item<EventItemViewModel>>> {
        val (items, pageNo, pageCount) = page
        return items.toObservable()
                .map(this::toEventItemViewModel)
                .map { wrapWithState(it) }
                .toPage(pageNo, pageCount)
    }

    private fun toEventItemViewModel(event: Event): EventItemViewModel {
        return EventItemViewModel(event) {
            Log.d(this::class.java.simpleName, "Event item clicked ${event.id}")
        }
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}


