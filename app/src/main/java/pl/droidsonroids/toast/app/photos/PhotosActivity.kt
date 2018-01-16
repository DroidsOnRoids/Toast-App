package pl.droidsonroids.toast.app.photos

import android.content.Context
import android.content.Intent
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.utils.NavigationRequest

class PhotosActivity : BaseActivity() {
    companion object {
        private const val PHOTOS_KEY = "photos_key"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.Photos): Intent {
            return Intent(context, PhotosActivity::class.java)
                    .putExtra(PHOTOS_KEY, navigationRequest.photos.toTypedArray())
        }
    }
}