package pl.droidsonroids.toast.viewmodels.photos

import android.databinding.ObservableField
import pl.droidsonroids.toast.data.dto.ImageDto

class SinglePhotoViewModel(
        val position: Long,
        val loadFromCache: ObservableField<Boolean>,
        val image: ImageDto,
        val onPhotoLoadingFinished: () -> Unit,
        private val action: () -> Unit
) {
    fun onClick() {
        action()
    }
}