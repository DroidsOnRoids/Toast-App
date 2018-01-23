package pl.droidsonroids.toast.viewmodels.event

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.app.utils.ParentView
import pl.droidsonroids.toast.data.dto.event.TalkDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerItemViewModel
import javax.inject.Inject

class TalkDetailsViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    val id: ObservableField<Long> = ObservableField()
    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val speakerItemViewModel: ObservableField<SpeakerItemViewModel> = ObservableField()

    private var eventId = Constants.NO_ID

    fun init(eventId: Long, talkDto: TalkDto) {
        this.eventId = eventId
        talkDto.let {
            id.set(it.id)
            title.set(it.title)
            description.set(it.description)
            speakerItemViewModel.set(it.speaker.toViewModel(::onSpeakerClick))
        }
    }

    private fun onSpeakerClick(speakerId: Long) {
        navigationSubject.onNext(NavigationRequest.SpeakerDetails(speakerId, eventId, ParentView.EVENT_DETAILS))
    }

    fun onReadLess() {
        navigationSubject.onNext(NavigationRequest.Close)
    }
}