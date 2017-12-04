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
import pl.droidsonrioids.toast.managers.EventsRepository
import javax.inject.Inject


class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {


    val featuredEvent = ObservableField<UpcomingEventViewModel>()
    val previousEvents: BehaviorSubject<List<State<Event>>> = BehaviorSubject.create()
    private var disposable: Disposable? = null
    private var nextPageNo: Int? = null

    init {
        disposable = eventsRepository.getEvents()
                .flatMap { (featuredEvent, previousEventsPage) ->
                    previousEventsPage
                            .mapToSinglePageWithState()
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
        nextPageNo?.takeIf { disposable?.isDisposed == true }?.let {
            disposable = eventsRepository.getEventsPage(it)
                    .flatMap { previousEventsPage ->
                        previousEventsPage
                                .mapToSinglePageWithState()
                    }
                    .doAfterTerminate { disposable?.dispose() }
                    .subscribeBy(
                            onSuccess = {
                                handleNewEventsPage(it)
                            },
                            onError = {
                                Log.e(this::class.java.simpleName, "Something went wrong with fetching previous events next page for EventsViewModel", it)
                            }
                    )
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
