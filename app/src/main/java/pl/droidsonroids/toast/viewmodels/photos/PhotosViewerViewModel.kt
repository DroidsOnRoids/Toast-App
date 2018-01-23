package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toSingleViewModel
import javax.inject.Inject

class PhotosViewerViewModel @Inject constructor() : ViewModel() {
    var singlePhotoViewModels: List<SinglePhotoViewModel> = emptyList()
        private set
    val photoLoadingFinishedSubject: PublishSubject<Long> = PublishSubject.create()

    fun init(photos: List<ImageDto>) {
        if (singlePhotoViewModels.isEmpty()) {
            singlePhotoViewModels = photos.mapIndexed(::imageDtoToViewModel)
        }
    }

    private fun imageDtoToViewModel(index: Int, image: ImageDto): SinglePhotoViewModel {
        return image.toSingleViewModel(index.toLong()) {
            onPhotoLoadingFinished(index.toLong())
        }
    }

    private fun onPhotoLoadingFinished(index: Long) {
        photoLoadingFinishedSubject.onNext(index)
    }

}