package pl.droidsonroids.toast.viewmodels.event

import pl.droidsonroids.toast.data.dto.ImageDto
import java.util.*

class EventItemViewModel(
        val id: Long,
        val title: String,
        val date: Date,
        val coverImage: ImageDto?,
        private val onEventClick: (Long) -> Unit,
        val onCoverLoadingFinish: () -> Unit) {
    fun onClick() {
        onEventClick(id)
    }
}
