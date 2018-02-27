package pl.droidsonroids.toast.viewmodels.event

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.event.CoordinatesDto
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.data.dto.event.EventTalkDto
import pl.droidsonroids.toast.data.enums.ParentView
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.repositories.event.EventsRepository
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.SourceAttending
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import pl.droidsonroids.toast.viewmodels.facebook.AttendViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

private const val DEFAULT_GRADIENT_COLOR = 0xA0000000.toInt()
private const val GRADIENT_COLOR_MASK = 0xE0FFFFFF.toInt()

class EventDetailsViewModel @Inject constructor(
        private val eventsRepository: EventsRepository,
        attendViewModel: AttendViewModel,
        private val analyticsEventTracker: AnalyticsEventTracker
) : ViewModel(), LoadingViewModel, NavigatingViewModel, AttendViewModel by attendViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = navigationRequests
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.PENDING)
    val eventId = ObservableField(Constants.NO_ID)
    val title = ObservableField("")
    val date = ObservableField<Date>()
    val placeName = ObservableField("")
    val placeStreet = ObservableField("")
    val coverImage = ObservableField<ImageDto?>()
    val photosAvailable = ObservableField(false)
    val gradientColor = ObservableField(DEFAULT_GRADIENT_COLOR)
    val loadFromCache = ObservableField(true)

    val coverImageLoadingFinishedSubject: PublishSubject<Unit> = PublishSubject.create()

    val onLoadingFinished: () -> Unit = {
        coverImageLoadingFinishedSubject.onNext(Unit)
    }

    val onGradientColorLoaded: (Int) -> Unit = {
        gradientColor.set(it and GRADIENT_COLOR_MASK)
    }
    private var coordinates: CoordinatesDto? = null
    val isSpeakersLabelVisible = ObservableField(false)

    val eventSpeakersSubject: BehaviorSubject<List<EventSpeakerItemViewModel>> = BehaviorSubject.create()

    var photos: List<ImageDto> = emptyList()

    private var eventsDisposable = Disposables.disposed()

    fun onPhotosClick() {
        navigationSubject.onNext(NavigationRequest.Photos(photos, eventId.get(), ParentView.EVENT_DETAILS))
        analyticsEventTracker.logEventDetailsSeePhotosEvent(eventId.get())
    }

    fun onLocationClick() {
        coordinates?.let {
            navigationSubject.onNext(NavigationRequest.Map(it, placeName.get()))
            analyticsEventTracker.logEventDetailsTapMeetupPlaceEvent()
        }
    }

    fun init(id: Long, coverImage: ImageDto?) {
        if (eventId.get() == Constants.NO_ID) {
            eventId.set(id)
            this.coverImage.set(coverImage)
            loadEvent()
        }
    }

    private fun loadEvent() {
        loadingStatus.set(LoadingStatus.PENDING)
        eventsDisposable = eventsRepository.getEvent(eventId.get())
                .subscribeBy(
                        onSuccess = (::onEventLoaded),
                        onError = (::onEventLoadError)
                )
    }

    private fun onEventLoaded(eventDetailsDto: EventDetailsDto) {
        loadingStatus.set(LoadingStatus.SUCCESS)
        eventDetailsDto.let {
            title.set(it.title)
            date.set(it.date)
            placeName.set(it.placeName)
            placeStreet.set(it.placeStreet)
            coverImage.run { set(get() ?: it.coverImages.firstOrNull()) }
            photosAvailable.set(it.photos.isNotEmpty())
            photos = it.photos
            coordinates = it.coordinates
            onTalksLoaded(it.talks)
            setEvent(it.facebookId, it.date, SourceAttending.EVENT_DETAILS)
        }
    }

    private fun onTalksLoaded(talks: List<EventTalkDto>) {
        val eventSpeakerViewModels = talks.map { it.toViewModel(::onReadMore, ::onSpeakerClick) }
        eventSpeakersSubject.onNext(eventSpeakerViewModels)
        isSpeakersLabelVisible.set(eventSpeakerViewModels.isNotEmpty())
    }

    private fun onReadMore(eventSpeakerItemViewModel: EventSpeakerItemViewModel) {
        val eventTalkDto = eventSpeakerItemViewModel.toDto()
        navigationSubject.onNext(NavigationRequest.EventTalkDetails(eventTalkDto))
        Timber.d("onReadMore: ${eventSpeakerItemViewModel.id}")
        analyticsEventTracker.logEventDetailsReadMoreEvent(eventTalkDto.title)
    }

    private fun onSpeakerClick(speakerId: Long, speakerName: String) {
        navigationSubject.onNext(NavigationRequest.SpeakerDetails(speakerId))
        analyticsEventTracker.logEventDetailsShowSpeakerEvent(speakerName)
    }

    private fun onEventLoadError(throwable: Throwable) {
        loadingStatus.set(LoadingStatus.ERROR)
        Timber.e(throwable, "Something went wrong when fetching event details with id = $eventId")
    }

    override fun retryLoading() {
        loadEvent()
    }

    override fun onCleared() {
        dispose()
        eventsDisposable.dispose()
    }

    fun onTransitionEnd() {
        loadFromCache.set(false)
    }
}