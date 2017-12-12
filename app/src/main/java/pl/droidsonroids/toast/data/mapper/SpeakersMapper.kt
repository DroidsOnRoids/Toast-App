package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto

fun ApiSpeaker.toDto(): SpeakerDto {
    return SpeakerDto(id, name, job, avatar.toDto())
}