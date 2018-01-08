package pl.droidsonroids.toast.viewmodels.event

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.event.TalkDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerItemViewModel
import javax.inject.Inject

class TalkDetailsViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    val title: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val speakerItemViewModel: ObservableField<SpeakerItemViewModel> = ObservableField()

    fun init(talkDto: TalkDto) {
        talkDto.let {
            title.set(it.title)
            description.set(it.description)
            speakerItemViewModel.set(it.speaker.toViewModel(::onSpeakerClick))
        }
    }

    private fun onSpeakerClick(speakerId: Long) {
        navigationSubject.onNext(NavigationRequest.SpeakerDetails(speakerId))
    }

    fun onReadLess() {
        navigationSubject.onNext(NavigationRequest.Close)
    }
}