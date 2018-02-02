package pl.droidsonroids.toast.viewmodels.speaker

import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.viewmodels.event.EventItemViewModel

class SpeakerTalkViewModel(
        val id: Long,
        val title: String,
        val description: String,
        val eventItemViewModel: EventItemViewModel,
        private val action: (SpeakerTalkDto) -> Unit
) {
    fun onReadMore() {
        action(toDto())
    }
}