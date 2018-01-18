package pl.droidsonroids.toast.viewmodels.photos

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import pl.droidsonroids.toast.data.dto.ImageDto
import javax.inject.Inject

class SinglePhotoViewModel @Inject constructor() : ViewModel() {
    val photo = ObservableField<ImageDto>()

    fun init(photo: ImageDto) {
        this.photo.set(photo)
    }

}