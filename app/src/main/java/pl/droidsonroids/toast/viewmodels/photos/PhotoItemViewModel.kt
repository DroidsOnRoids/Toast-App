package pl.droidsonroids.toast.viewmodels.photos

import pl.droidsonroids.toast.data.dto.ImageDto

class PhotoItemViewModel(
        val position: Int,
        val image: ImageDto,
        private val onPhotoClick: (Int) -> Unit
) {
    fun onClick() {
        onPhotoClick(position)
    }
}