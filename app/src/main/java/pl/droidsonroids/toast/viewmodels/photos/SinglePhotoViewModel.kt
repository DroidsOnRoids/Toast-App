package pl.droidsonroids.toast.viewmodels.photos

import pl.droidsonroids.toast.data.dto.ImageDto

class SinglePhotoViewModel(
        val position: Long,
        val image: ImageDto,
        val onPhotoLoadingFinished: () -> Unit
)