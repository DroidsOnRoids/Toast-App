package pl.droidsonroids.toast.app.photos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.MenuItem
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_photos_viewer.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.photos.PhotosViewerViewModel
import javax.inject.Inject

class PhotosViewerActivity : BaseActivity() {
    companion object {
        private const val PHOTOS_KEY = "photo_key"
        private const val CURRENT_PHOTO_POSITION_KEY = "current_photo_position_key"

        private const val IMMERSIVE_MODE =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        private const val NORMAL_MODE = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SinglePhoto): Intent {
            return Intent(context, PhotosViewerActivity::class.java)
                    .putExtra(PHOTOS_KEY, ArrayList(navigationRequest.photos))
                    .putExtra(CURRENT_PHOTO_POSITION_KEY, navigationRequest.position)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val photosViewerViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PhotosViewerViewModel::class.java]
    }

    private val currentPosition by lazy {
        intent.getLongExtra(CURRENT_PHOTO_POSITION_KEY, 0L)
    }

    private var isTransitionPostponed = false

    private val compositeDisposable = CompositeDisposable()

    private val isImmersiveMode get() = window.decorView.systemUiVisibility and IMMERSIVE_MODE == IMMERSIVE_MODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_viewer)
        postponeSharedTransition()
        setupToolbar()
        setupViewModel()
        setupViewPager()
        setupWindow()
    }

    private fun postponeSharedTransition() {
        supportPostponeEnterTransition()
        isTransitionPostponed = true
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        val photos = intent.getParcelableArrayListExtra<ImageDto>(PHOTOS_KEY)
        photosViewerViewModel.init(photos)

        compositeDisposable += photosViewerViewModel.photoLoadingFinishedSubject
                .filter { currentPosition == it && isTransitionPostponed }
                .subscribe { resumeSharedTransition() }

        compositeDisposable += photosViewerViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
    }

    private fun resumeSharedTransition() {
        supportStartPostponedEnterTransition()
        isTransitionPostponed = false
    }

    private fun handleNavigationRequest(navigationRequest: NavigationRequest) {
        if (navigationRequest == NavigationRequest.ToggleImmersive) {
            toggleImmersiveMode()
        } else {
            navigator.dispatch(this, navigationRequest)
        }
    }

    private fun setupViewPager() {
        with(photosViewPager) {
            val singlePhotoViewModels = photosViewerViewModel.singlePhotoViewModels
            val photosViewPagerAdapter = PhotosViewPagerAdapter(singlePhotoViewModels)
            adapter = photosViewPagerAdapter
            currentItem = currentPosition.toInt()
            pageMargin = resources.getDimensionPixelSize(R.dimen.margin_large)
            offscreenPageLimit = 2
        }
    }

    private fun setupWindow() {
        window.decorView.systemUiVisibility = NORMAL_MODE
        ViewCompat.setOnApplyWindowInsetsListener(photosViewPager) { _, insets ->
            for (i in 0 until photosViewPager.childCount) {
                ViewCompat.dispatchApplyWindowInsets(photosViewPager.getChildAt(i), insets)
            }
            insets
        }
    }

    private fun toggleImmersiveMode() {
        window.decorView.systemUiVisibility = if (isImmersiveMode) NORMAL_MODE else IMMERSIVE_MODE
        val appBarTranslation = if (isImmersiveMode) -appBar.bottom.toFloat() else 0.0f
        appBar.animate().translationY(appBarTranslation).start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> consume { finish() }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isImmersiveMode) {
            toggleImmersiveMode()
        } else {
            finish()
        }
    }
}

