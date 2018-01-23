package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import javax.inject.Inject

class PhotosViewModel @Inject constructor() : ViewModel() {
    private val Any.simpleClassName get() = this::class.java.simpleName

    val photosSubject: BehaviorSubject<List<PhotoItemViewModel>> = BehaviorSubject.create()

    fun init(photos: List<ImageDto>) {
        if (photosSubject.value == null) {
            photosSubject.onNext(photos.map { it.toViewModel(::onPhotoItemClicked) })
        }
    }

    private fun onPhotoItemClicked() {
        Log.d(simpleClassName, "On photos clicked")
    }

}