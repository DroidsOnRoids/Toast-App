package pl.droidsonroids.toast.viewmodels.event

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.data.dto.event.TalkDto
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import java.util.*
import javax.inject.Inject

private const val DEFAULT_GRADIENT_COLOR = 0xA0000000.toInt()
private const val GRADIENT_COLOR_MASK = 0xE0FFFFFF.toInt()

class EventDetailsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel(), LoadingViewModel, NavigatingViewModel {
    private val Any.simpleClassName: String get() = javaClass.simpleName
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.PENDING)
    private var eventId: Long? = null
    val title = ObservableField("")
    val date = ObservableField<Date>()
    val placeName = ObservableField("")
    val placeStreet = ObservableField("")
    val coverImage = ObservableField<ImageDto?>()
    val photosAvailable = ObservableField(false)
    val gradientColor = ObservableField(DEFAULT_GRADIENT_COLOR)
    val onGradientColorLoaded: (Int) -> Unit = {
        gradientColor.set(it and GRADIENT_COLOR_MASK)
    }
    val eventSpeakersSubject: BehaviorSubject<List<EventSpeakerItemViewModel>> = BehaviorSubject.create()

    fun onPhotosClick() {
        Log.d(simpleClassName, "On photos clicked")
    }

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
                            onError = (::onEventLoadError)
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
            photosAvailable.set(it.photos.isNotEmpty())
            onTalksLoaded(it.talks)
        }
    }

    private fun onTalksLoaded(talks: List<TalkDto>) {
        val eventSpeakerViewModels = talks.map { it.toViewModel(::onReadMore, ::onSpeakerClick) }
        eventSpeakersSubject.onNext(eventSpeakerViewModels)
    }

    private fun onReadMore(eventSpeakerItemViewModel: EventSpeakerItemViewModel) {
        navigationSubject.onNext(NavigationRequest.TalkDetails(eventSpeakerItemViewModel.toDto()))
        Log.d(simpleClassName, "onReadMore: ${eventSpeakerItemViewModel.id}")
    }

    private fun onSpeakerClick(speakerId: Long) {
        navigationSubject.onNext(NavigationRequest.SpeakerDetails(speakerId))
    }

    private fun onEventLoadError(throwable: Throwable) {
        loadingStatus.set(LoadingStatus.ERROR)
        Log.e(simpleClassName, "Something went wrong when fetching event details with id = $eventId", throwable)
    }

    override fun retryLoading() {
        loadEvent()
    }
}