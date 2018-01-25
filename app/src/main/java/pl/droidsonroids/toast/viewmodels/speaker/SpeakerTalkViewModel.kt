package pl.droidsonroids.toast.viewmodels.speaker

import pl.droidsonroids.toast.viewmodels.event.EventItemViewModel

class SpeakerTalkViewModel(
        val id: Long,
        val title: String,
        val description: String,
        val eventItemViewModel: EventItemViewModel
)