package pl.droidsonroids.toast.viewmodels

import pl.droidsonroids.toast.data.dto.ImageDto
import java.util.*

class EventItemViewModel(
        val id: Long,
        val title: String,
        val date: Date,
        val coverImage: ImageDto?,
        private val action: (Long) -> Unit) {
    fun onClick() {
        action(id)
    }
}
