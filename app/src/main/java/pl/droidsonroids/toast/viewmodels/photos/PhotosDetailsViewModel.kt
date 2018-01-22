package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toSingleViewModel
import javax.inject.Inject

class PhotosDetailsViewModel @Inject constructor() : ViewModel() {
    var singlePhotoViewModels: List<SinglePhotoViewModel> = emptyList()
    val photoLoadedSubject: PublishSubject<Long> = PublishSubject.create()

    fun init(photos: List<ImageDto>) {
        if (singlePhotoViewModels.isEmpty()) {
            singlePhotoViewModels = photos.mapIndexed { index, image -> image.toSingleViewModel(index.toLong()) { onPhotoLoaded(index.toLong()) } }
        }
    }

    private fun onPhotoLoaded(index: Long) {
        photoLoadedSubject.onNext(index)
    }

}