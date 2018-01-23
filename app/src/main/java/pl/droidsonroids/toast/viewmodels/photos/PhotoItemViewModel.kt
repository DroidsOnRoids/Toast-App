package pl.droidsonroids.toast.viewmodels.photos

import pl.droidsonroids.toast.data.dto.ImageDto

class PhotoItemViewModel(
        val position: Long,
        val image: ImageDto,
        private val action: (Long) -> Unit
) {
    fun onClick() {
        action(position)
    }
}