package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.api.speaker.ApiSpeakerDetails
import pl.droidsonroids.toast.data.api.speaker.ApiSpeakerTalk
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDetailsDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerItemViewModel

fun ApiSpeaker.toDto(): SpeakerDto {
    return SpeakerDto(
            id = id,
            name = name,
            job = job,
            avatar = avatar.toDto()
    )
}

fun ApiSpeakerDetails.toDto(): SpeakerDetailsDto {
    val talksDto = talks.map { it.toDto() }
    return SpeakerDetailsDto(
            id = id,
            name = name,
            job = job,
            bio = bio,
            avatar = avatar.toDto(),
            talks = talksDto
    )
}

fun ApiSpeakerTalk.toDto(): SpeakerTalkDto {
    return SpeakerTalkDto(
            id = id,
            title = title,
            description = description,
            event = event.toDto()
    )
}

fun SpeakerDto.toViewModel(onClick: (Long) -> Unit): SpeakerItemViewModel {
    return SpeakerItemViewModel(
            id = id,
            name = name,
            job = job,
            avatar = avatar,
            action = onClick
    )
}

fun SpeakerItemViewModel.toDto(): SpeakerDto {
    return SpeakerDto(
            id = id,
            name = name,
            job = job,
            avatar = avatar
    )
}