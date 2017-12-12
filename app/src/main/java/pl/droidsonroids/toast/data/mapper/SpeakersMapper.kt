package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerItemViewModel

fun ApiSpeaker.toDto(): SpeakerDto {
    return SpeakerDto(id, name, job, avatar.toDto())
}

fun SpeakerDto.toViewModel(onClick: (Long) -> Unit): SpeakerItemViewModel {
    return SpeakerItemViewModel(
            id,
            name,
            job,
            avatar,
            onClick
    )
}