package pl.droidsonroids.toast.viewmodels.photos

class PhotoItemViewModel(val originalSizeUrl: String,
                         val thumbSizeUrl: String,
                         private val action: () -> Unit) {
    fun onClick() {
        action()
    }
}