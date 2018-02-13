package pl.droidsonroids.toast.viewmodels.photos

import pl.droidsonroids.toast.data.dto.ImageDto

class PhotoItemViewModel(
        val position: Long,
        val image: ImageDto,
        private val onPhotoClick: (Long) -> Unit
) {
    fun onClick() {
        onPhotoClick(position)
    }
}