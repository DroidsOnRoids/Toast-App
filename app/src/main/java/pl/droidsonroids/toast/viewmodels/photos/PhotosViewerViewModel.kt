package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toSingleViewModel
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class PhotosViewerViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    var singlePhotoViewModels: List<SinglePhotoViewModel> = emptyList()
        private set
    val photoLoadingFinishedSubject: PublishSubject<Long> = PublishSubject.create()
    private val loadFromCache = ObservableField(true)

    fun init(photos: List<ImageDto>) {
        if (singlePhotoViewModels.isEmpty()) {
            singlePhotoViewModels = photos.mapIndexed(::imageDtoToViewModel)
        }
    }

    private fun imageDtoToViewModel(index: Int, image: ImageDto): SinglePhotoViewModel {
        return image.toSingleViewModel(
                index.toLong(),
                loadFromCache,
                { onPhotoLoadingFinished(index.toLong()) },
                ::onClick
        )
    }

    fun onTransitionEnd() {
        loadFromCache.set(false)
    }

    private fun onPhotoLoadingFinished(index: Long) {
        photoLoadingFinishedSubject.onNext(index)
    }

    fun onClick() {
        navigationSubject.onNext(NavigationRequest.ToggleImmersive)
    }

}