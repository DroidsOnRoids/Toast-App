package pl.droidsonroids.toast.app.photos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.databinding.ActivitySinglePhotoBinding
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.photos.SinglePhotoViewModel

class SinglePhotoActivity : BaseActivity() {
    companion object {
        private const val PHOTO_KEY = "photo_key"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SinglePhoto): Intent {
            return Intent(context, SinglePhotoActivity::class.java)
                    .putExtra(PHOTO_KEY, navigationRequest.image)
        }
    }

    private val photo by lazy {
        intent.getParcelableExtra<ImageDto>(PHOTO_KEY)
    }

    private val singlePhotoViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SinglePhotoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val singlePhotoBinding = ActivitySinglePhotoBinding.inflate(layoutInflater)
        setContentView(singlePhotoBinding.root)
        setupViewModel(singlePhotoBinding)
    }

    private fun setupViewModel(singlePhotoBinding: ActivitySinglePhotoBinding) {
        singlePhotoViewModel.init(photo)
        singlePhotoBinding.singlePhotoViewModel = singlePhotoViewModel
    }
}