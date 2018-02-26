package pl.droidsonroids.toast.viewmodels.photos

import pl.droidsonroids.toast.data.dto.ImageDto

class FullscreenPhotoViewModel(
        val position: Long,
        val image: ImageDto,
        private val onPhotoClick: () -> Unit
) {
    fun onClick() {
        onPhotoClick()
    }
}