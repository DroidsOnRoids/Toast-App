package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toFullscreenViewModel
import pl.droidsonroids.toast.data.mapper.toItemViewModel
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class PhotosViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    val photosSubject: BehaviorSubject<List<PhotoItemViewModel>> = BehaviorSubject.create()
    val fullscreenPhotosSubject: BehaviorSubject<List<FullscreenPhotoViewModel>> = BehaviorSubject.create()

    private var photosDto: List<ImageDto> = emptyList()

    fun init(photos: List<ImageDto>) {
        if (!photosSubject.hasValue()) {
            photosDto = photos
            val photosViewModels = photos.mapIndexed(::imageDtoToViewModel)
            val fullPhotosViewModels = photos.mapIndexed(::fullImageDtoToViewModel)
            photosSubject.onNext(photosViewModels)
            fullscreenPhotosSubject.onNext(fullPhotosViewModels)
        }
    }

    private fun imageDtoToViewModel(index: Int, image: ImageDto) =
            image.toItemViewModel(index, ::onPhotoItemClicked)

    private fun fullImageDtoToViewModel(index: Int, image: ImageDto) =
            image.toFullscreenViewModel(index, ::onFullscreenPhotoClick)

    fun onFullscreenPhotoClick() {
        navigationSubject.onNext(NavigationRequest.ToggleImmersive)
    }

    fun onPhotoItemClicked(index: Int) {
        navigationSubject.onNext(NavigationRequest.FullscreenPhoto(index))
    }

}