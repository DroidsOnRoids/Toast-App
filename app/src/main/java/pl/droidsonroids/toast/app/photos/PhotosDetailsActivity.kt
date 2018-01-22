package pl.droidsonroids.toast.app.photos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_photos_details.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.photos.PhotosDetailsViewModel

class PhotosDetailsActivity : BaseActivity() {
    companion object {
        private const val PHOTOS_KEY = "photo_key"
        private const val CURRENT_PHOTO_POSITION_KEY = "current_photo_position_key"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SinglePhoto): Intent {
            return Intent(context, PhotosDetailsActivity::class.java)
                    .putExtra(PHOTOS_KEY, ArrayList(navigationRequest.photos))
                    .putExtra(CURRENT_PHOTO_POSITION_KEY, navigationRequest.position)
        }
    }

    private val photosDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PhotosDetailsViewModel::class.java]
    }

    private val photos by lazy {
        intent.getParcelableArrayListExtra<ImageDto>(PHOTOS_KEY)
    }

    private val currentPosition by lazy {
        intent.getLongExtra(CURRENT_PHOTO_POSITION_KEY, 0L)
    }

    private val viewPagerPageMargin by lazy {
        resources.getDimensionPixelSize(R.dimen.margin_large)
    }

    private var isTransitionPostponed = false

    private var disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_details)
        postponeSharedTransition()
        setupViewModel()
        setupViewPager()
    }

    private fun setupViewModel() {
        photosDetailsViewModel.init(photos)
        disposable = photosDetailsViewModel.photoLoadedSubject
                .filter { currentPosition == it && isTransitionPostponed }
                .subscribe { resumeSharedTransition() }
    }

    private fun resumeSharedTransition() {
        supportStartPostponedEnterTransition()
        isTransitionPostponed = false
    }

    private fun postponeSharedTransition() {
        supportPostponeEnterTransition()
        isTransitionPostponed = true
    }

    private fun setupViewPager() {
        with(photosViewPager) {
            val singlePhotoViewModels = photosDetailsViewModel.singlePhotoViewModels
            val photosViewPagerAdapter = PhotosViewPagerAdapter(singlePhotoViewModels)
            adapter = photosViewPagerAdapter
            currentItem = currentPosition.toInt()
            pageMargin = viewPagerPageMargin
            offscreenPageLimit = 2
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
    }
}

