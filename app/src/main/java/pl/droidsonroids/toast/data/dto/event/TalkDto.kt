package pl.droidsonroids.toast.data.dto.event

import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto

data class TalkDto(
        val id: Long,
        val title: String,
        val description: String,
        val speaker: SpeakerDto
)