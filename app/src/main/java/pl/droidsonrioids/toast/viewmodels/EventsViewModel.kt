package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonrioids.toast.data.model.LoadingStatus
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.managers.EventsRepository
import javax.inject.Inject

class EventsViewModel @Inject constructor(eventsRepository: EventsRepository) : ViewModel() {

    var loadingStatus: ObservableField<LoadingStatus> = ObservableField()
    var featuredEvent: ObservableField<UpcomingEventViewModel> = ObservableField()
        private set
    // TODO:  TOA-42 Add previous events handling
    var lastEvents: List<EventDto> = emptyList()
        private set
    private val eventsDisposable: Disposable

    init {
        loadingStatus.set(LoadingStatus.PENDING)
        eventsDisposable = eventsRepository.getEvents()
                .subscribeBy(
                        onSuccess = {
                            featuredEvent.set(UpcomingEventViewModel.create(it.upcomingEvent))
                            lastEvents = it.lastEvents
                            loadingStatus.set(LoadingStatus.SUCCESS)
                        },
                        onError = {
                            loadingStatus.set(LoadingStatus.ERROR)
                            Log.e(this::class.java.simpleName, "Something went wrong with fetching data for EventsViewModel", it)
                        }
                )
    }

    override fun onCleared() {
        eventsDisposable.dispose()
    }

}