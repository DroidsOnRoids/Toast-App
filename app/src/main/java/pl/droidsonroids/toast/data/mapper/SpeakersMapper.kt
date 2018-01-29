package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.api.speaker.ApiSpeakerDetails
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDetailsDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
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
    return SpeakerDetailsDto(
            id = id,
            name = name,
            job = job,
            bio = bio,
            avatar = avatar.toDto(),
            github = github,
            website = website,
            twitter = twitter,
            email = email
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