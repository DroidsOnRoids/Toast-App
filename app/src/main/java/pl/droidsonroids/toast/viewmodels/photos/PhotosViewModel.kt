package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toItemViewModel
import pl.droidsonroids.toast.data.mapper.toSingleViewModel
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class PhotosViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    val photosSubject: BehaviorSubject<List<PhotoItemViewModel>> = BehaviorSubject.create()
    val fullPhotosSubject: BehaviorSubject<List<SinglePhotoViewModel>> = BehaviorSubject.create()
    private lateinit var onPhotoItemClicked: (Long) -> Unit

    private var photosDto: List<ImageDto> = emptyList()

    var isSharedTransitionInProgress = false

    fun init(photos: List<ImageDto>, onPhotoItemClicked: (Long) -> Unit) {
        if (!photosSubject.hasValue()) {
            photosDto = photos
            this.onPhotoItemClicked = onPhotoItemClicked
            val photosViewModels = photos.mapIndexed(::imageDtoToViewModel)
            val fullPhotosViewModels = photos.mapIndexed(::fullImageDtoToViewModel)
            photosSubject.onNext(photosViewModels)
            fullPhotosSubject.onNext(fullPhotosViewModels)
        }
    }

    private fun imageDtoToViewModel(index: Int, image: ImageDto) =
            image.toItemViewModel(index.toLong(), onPhotoItemClicked)

    private fun fullImageDtoToViewModel(index: Int, image: ImageDto): SinglePhotoViewModel {
        return image.toSingleViewModel(
                position = index.toLong(),
                loadFromCache = ObservableField(false),
                onPhotoLoadingFinished = { },
                onClick = ::onClick
        )
    }

    //    private fun onPhotoItemClicked(position: Long) {
    //        if (!isSharedTransitionInProgress) {
    //            isSharedTransitionInProgress = true
    //            navigationSubject.onNext(NavigationRequest.SinglePhoto(photosDto, position))
    //        }
    //    }

    fun onClick() {
        navigationSubject.onNext(NavigationRequest.ToggleImmersive)
    }

}