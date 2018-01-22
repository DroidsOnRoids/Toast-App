package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toItemViewModel
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class PhotosViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    val photosSubject: BehaviorSubject<List<PhotoItemViewModel>> = BehaviorSubject.create()

    private var photosDto: List<ImageDto> = emptyList()

    fun init(photos: List<ImageDto>) {
        if (!photosSubject.hasValue()) {
            photosDto = photos
            photosSubject.onNext(photos.mapIndexed { index, image -> image.toItemViewModel(index.toLong(), ::onPhotoItemClicked) })
        }
    }

    private fun onPhotoItemClicked(position: Long) {
        navigationSubject.onNext(NavigationRequest.SinglePhoto(photosDto, position))
    }

}