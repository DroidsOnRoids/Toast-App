package pl.droidsonroids.toast.data.dto.speaker

import pl.droidsonroids.toast.data.dto.ImageDto

data class SpeakerDto(
        val id: Long,
        val name: String,
        val job: String,
        val avatar: ImageDto
)