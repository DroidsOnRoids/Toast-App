package pl.droidsonroids.toast.viewmodels.event

import android.databinding.ObservableField
import pl.droidsonroids.toast.data.dto.ImageDto
import java.util.*

class EventItemViewModel(
        val id: Long,
        val title: String,
        val date: Date,
        val coverImage: ImageDto?,
        private val onEventClick: (Long, ImageDto?) -> Unit,
        private val onCoverLoadingFinishCallback: () -> Unit) {
    val loadFromCache = ObservableField(true)
    val onCoverLoadingFinish = {
        loadFromCache.set(false)
        onCoverLoadingFinishCallback()
    }

    fun onClick() {
        onEventClick(id, coverImage)
    }
}
