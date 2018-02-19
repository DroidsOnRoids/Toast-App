package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import pl.droidsonroids.toast.viewmodels.event.EventItemViewModel
import javax.inject.Inject


class SpeakerTalkDetailsViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    val id: ObservableField<Long> = ObservableField()
    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val eventItemViewModel: ObservableField<EventItemViewModel> = ObservableField()

    fun init(talkDto: SpeakerTalkDto, onCoverLoadingFinished: () -> Unit) {
        talkDto.let {
            id.set(it.id)
            title.set(it.title)
            description.set(it.description)
            eventItemViewModel.set(it.event.toViewModel(::onEventClick, onCoverLoadingFinished))
        }
    }

    private fun onEventClick(eventId: Long, imageDto: ImageDto?) {
        navigationSubject.onNext(NavigationRequest.EventDetails(eventId, imageDto))
    }

    fun onReadLess() {
        navigationSubject.onNext(NavigationRequest.Close)
    }
}