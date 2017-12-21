package pl.droidsonroids.toast.viewmodels.event

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import java.util.*
import javax.inject.Inject

class EventDetailsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel(), LoadingViewModel {
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField()
    private var eventId: Long? = null
    val title: ObservableField<String> = ObservableField()
    val date: ObservableField<Date> = ObservableField()
    val placeName: ObservableField<String> = ObservableField()
    val placeStreet: ObservableField<String> = ObservableField()
    val coverImage: ObservableField<ImageDto?> = ObservableField()
    val gradientColor: ObservableField<Int> = ObservableField(0xFF000000.toInt())
    val onPaletteLoaded: (Int) -> Unit = {
        gradientColor.set(it and 0xE0FFFFFF.toInt())
    }

    private val Any.simpleClassName: String get() = javaClass.simpleName


    fun init(id: Long) {
        if (eventId == null) {
            eventId = id
            loadEvent()
        }
    }

    private fun loadEvent() {
        eventId?.let {
            loadingStatus.set(LoadingStatus.PENDING)
            eventsRepository.getEvent(it)
                    .subscribeBy(
                            onSuccess = (::onEventLoaded),
                            onError = { onEventLoadError(it) }
                    )
        }
    }

    private fun onEventLoaded(eventDetailsDto: EventDetailsDto) {
        loadingStatus.set(LoadingStatus.SUCCESS)
        eventDetailsDto.let {
            title.set(it.title)
            date.set(it.date)
            placeName.set(it.placeName)
            placeStreet.set(it.placeStreet)
            coverImage.set(it.coverImages.firstOrNull())
        }
    }

    private fun onEventLoadError(throwable: Throwable) {
        loadingStatus.set(LoadingStatus.ERROR)
        Log.e(simpleClassName, "Something went wrong when fetching event details with id = $eventId", throwable)
    }

    override fun retryLoading() {
        loadEvent()
    }
}