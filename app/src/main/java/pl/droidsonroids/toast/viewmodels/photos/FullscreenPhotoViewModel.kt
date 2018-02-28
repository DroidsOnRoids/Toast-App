package pl.droidsonroids.toast.viewmodels.photos

import pl.droidsonroids.toast.data.dto.ImageDto

class FullscreenPhotoViewModel(
        val position: Int,
        val image: ImageDto,
        private val onPhotoClick: () -> Unit
) {
    fun onClick() {
        onPhotoClick()
    }
}