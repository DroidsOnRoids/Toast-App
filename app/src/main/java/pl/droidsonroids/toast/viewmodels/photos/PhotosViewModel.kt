package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class PhotosViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    val photosSubject: BehaviorSubject<List<PhotoItemViewModel>> = BehaviorSubject.create()

    fun init(photos: List<ImageDto>) {
        if (photosSubject.value == null) {
            photosSubject.onNext(photos.map { it.toViewModel(::onPhotoItemClicked) })
        }
    }

    private fun onPhotoItemClicked(image: ImageDto) {
        navigationSubject.onNext(NavigationRequest.SinglePhoto(image))
    }

}