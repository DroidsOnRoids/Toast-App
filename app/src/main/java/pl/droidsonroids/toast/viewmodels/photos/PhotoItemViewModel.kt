package pl.droidsonroids.toast.viewmodels.photos

import pl.droidsonroids.toast.data.dto.ImageDto

class PhotoItemViewModel(
        val image: ImageDto,
        private val action: (ImageDto) -> Unit
) {
    fun onClick() {
        action(image)
    }
}