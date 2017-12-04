package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.Single
import android.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonrioids.toast.data.model.Event
import pl.droidsonrioids.toast.data.model.Page
import pl.droidsonrioids.toast.data.model.State
import pl.droidsonrioids.toast.managers.EventsRepository
import javax.inject.Inject


class EventsViewModel @Inject constructor(eventsRepository: EventsRepository) : ViewModel() {


    val featuredEvent = ObservableField<UpcomingEventViewModel>()
    // TODO:  TOA-42 Add previous events handling
    val previousEvents: BehaviorSubject<List<State<Event>>> = BehaviorSubject.create()
    private var disposable: Disposable? = null
    private var nextPageNo: Int? = null

    init {
        disposable = eventsRepository.getEvents()
                .subscribeBy(
                        onSuccess = {
                            featuredEvent.set(UpcomingEventViewModel(it.upcomingEvent))
                            lastEvents = it.lastEvents
                        },
                        onError = {
                            Log.e(this::class.java.simpleName, "Something went wrong with fetching data for EventsViewModel", it)
                        }
                )
        disposable = eventsManager.getEvents()
                .flatMap { (featuredEvent, previousEventsPage) ->
                    previousEventsPage
                            .mapToSinglePageWithState()
                            .map { featuredEvent to it }
                            .toMaybe()
                }
                .subscribeBy(onSuccess = {
                    featuredEvent.set(UpcomingEventViewModel(it.first))
                    handleNewEventsPage(it.second)
                    disposable?.dispose()
                })
    }

    private fun handleNewEventsPage(it: Page<State<Event>>) {
        var newList =
                (previousEvents.value
                        ?.filter { it is State.Item }
                        ?: listOf()) + it.items
        if (it.pageNo < it.pageCount) {

            newList += State.Loading
        }
        previousEvents.onNext(newList)
    }

    fun loadNextPage() {
        nextPageNo?.takeIf { disposable?.isDisposed ?: false }?.let {
            disposable = eventsManager.getEventsPage(it)
                    .flatMap { previousEventsPage ->
                        previousEventsPage
                                .mapToSinglePageWithState()
                    }.subscribeBy(onSuccess = {
                handleNewEventsPage(it)
            })
        }
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}

private fun <E : Any> Page<E>.mapToSinglePageWithState(): Single<Page<State<E>>> {
    return items.toObservable()
            .map { State.Item(it) as State<E> }
            .toList()
            .map { Page(it, pageNo, pageCount) }
}
