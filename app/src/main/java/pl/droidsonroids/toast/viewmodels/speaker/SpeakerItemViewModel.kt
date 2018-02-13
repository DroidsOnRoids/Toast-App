package pl.droidsonroids.toast.viewmodels.speaker

import pl.droidsonroids.toast.data.dto.ImageDto

class SpeakerItemViewModel(
        val id: Long,
        val name: String,
        val job: String,
        val avatar: ImageDto,
        private val onSpeakerClick: (Long) -> Unit
) {
    fun onClick() {
        onSpeakerClick(id)
    }
}