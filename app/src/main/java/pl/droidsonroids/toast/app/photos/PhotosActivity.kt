package pl.droidsonroids.toast.app.photos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import com.alexvasilkov.gestures.transition.GestureTransitions
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_photos.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.app.utils.extensions.setVisibility
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.enums.ParentView
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.photos.PhotosViewModel
import java.util.*
import javax.inject.Inject

class PhotosActivity : BaseActivity() {
    companion object {
        private const val PHOTOS_KEY = "photos_key"
        private const val EVENT_ID_KEY = "event_id_key"
        private const val PARENT_VIEW_KEY = "parent_view_key"
        private const val PHOTOS_GRID_SIZE = 2

        private const val IMMERSIVE_MODE =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        private const val NORMAL_MODE = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        fun createIntent(context: Context, navigationRequest: NavigationRequest.Photos): Intent {
            return Intent(context, PhotosActivity::class.java)
                    .putExtra(EVENT_ID_KEY, navigationRequest.eventId)
                    .putParcelableArrayListExtra(PHOTOS_KEY, ArrayList(navigationRequest.photos))
                    .putExtra(PARENT_VIEW_KEY, navigationRequest.parentView)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val parentEventId by lazy { intent.getLongExtra(EVENT_ID_KEY, Constants.NO_ID) }
    private val photosViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PhotosViewModel::class.java]
    }

    private val photos by lazy { intent.getParcelableArrayListExtra<ImageDto>(PHOTOS_KEY) }
    private val parentView by lazy { intent.getSerializableExtra(PARENT_VIEW_KEY) }

    private val compositeDisposable = CompositeDisposable()
    private val isImmersiveMode
        get() = window.decorView.systemUiVisibility and PhotosActivity.IMMERSIVE_MODE == PhotosActivity.IMMERSIVE_MODE

    private lateinit var listAnimator: ViewsTransitionAnimator<Int>
    private lateinit var photosViewPagerAdapter: PhotosViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        setupWindow()
        setupToolbar()
        setupViewModel()
        setupRecyclerView()
        setupViewPager()
        setupPagerAnimator()
    }

    override fun onResume() {
        super.onResume()
        photosViewModel.isSharedTransitionInProgress = false
    }

    private fun setupWindow() {
        window.sharedElementsUseOverlay = false
        window.decorView.systemUiVisibility = PhotosActivity.NORMAL_MODE
        ViewCompat.setOnApplyWindowInsetsListener(fullPhotoViewPager) { _, insets ->
            for (i in 0 until fullPhotoViewPager.childCount) {
                ViewCompat.dispatchApplyWindowInsets(fullPhotoViewPager.getChildAt(i), insets)
            }
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(photosToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        photosViewModel.init(photos, ::onPhotoItemClicked)
        compositeDisposable += photosViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
    }

    private fun setupRecyclerView() {
        with(photosRecyclerView) {
            val photosAdapter = PhotosAdapter()
            adapter = photosAdapter
            layoutManager = GridLayoutManager(context, PHOTOS_GRID_SIZE)

            subscribeToPhotosChange(photosAdapter)
        }
    }

    private fun subscribeToPhotosChange(photosAdapter: PhotosAdapter) {
        compositeDisposable += photosViewModel.photosSubject
                .subscribe { photosAdapter.setData(it) }
    }

    private fun setupViewPager() {
        photosViewPagerAdapter = PhotosViewPagerAdapter(photosViewModel.fullscreenPhotosSubject.value)
        fullPhotoViewPager.adapter = photosViewPagerAdapter
        fullPhotoViewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.margin_large)
        photosViewPagerAdapter.notifyDataSetChanged()

        subscribeToFullscreenPhotosChange()
    }

    private fun subscribeToFullscreenPhotosChange() {
        compositeDisposable += photosViewModel.fullscreenPhotosSubject
                .subscribe { this.photosViewPagerAdapter.setData(it) }
    }

    private fun setupPagerAnimator() {
        listAnimator = GestureTransitions.from<Int>(photosRecyclerView, getRecyclerViewTracker())
                .into(fullPhotoViewPager, getViewPagerTracker())
        listAnimator.addPositionUpdateListener(::applyFullPhotoPagerState)
    }

    private fun getRecyclerViewTracker(): SimpleTracker {
        return object : SimpleTracker() {
            public override fun getViewAt(position: Int): View? {
                return photosRecyclerView.findViewHolderForLayoutPosition(position).itemView
            }
        }
    }

    private fun getViewPagerTracker(): SimpleTracker {
        return object : SimpleTracker() {
            public override fun getViewAt(position: Int): View? {
                val holder = photosViewPagerAdapter.getViewHolder(position)
                return if (holder == null) {
                    null
                } else {
                    photosViewPagerAdapter.getPhotoView(holder)
                }
            }
        }
    }

    private fun applyFullPhotoPagerState(position: Float, isLeaving: Boolean) {
        val isFullPhotoVisible = position != 0f

        fullPhotoBackground.setVisibility(isFullPhotoVisible)
        fullPhotoBackground.alpha = position

        fullPhotoToolbar.setVisibility(isFullPhotoVisible)
        fullPhotoToolbar.alpha = position

        if (isLeaving && isFullPhotoVisible && isImmersiveMode) {
            toggleImmersiveMode()
        }
    }

    private fun toggleImmersiveMode() {
        window.decorView.systemUiVisibility = if (isImmersiveMode) PhotosActivity.NORMAL_MODE else PhotosActivity.IMMERSIVE_MODE
        val appBarTranslation = if (isImmersiveMode) -fullPhotoToolbar.bottom.toFloat() else 0.0f
        fullPhotoToolbar.animate().translationY(appBarTranslation).start()
    }

    private fun onPhotoItemClicked(index: Long) {
        listAnimator.enter(index.toInt(), true)
    }

    private fun handleNavigationRequest(navigationRequest: NavigationRequest) {
        when (navigationRequest) {
            NavigationRequest.ToggleImmersive -> {
                toggleImmersiveMode()
            }
            else -> navigator.dispatch(this, navigationRequest)
        }
    }

    override fun onBackPressed() {
        if (listAnimator.isLeaving) {
            super.onBackPressed()
        } else {
            listAnimator.exit(true)
        }
        if (isImmersiveMode) {
            toggleImmersiveMode()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> handleUpAction()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleUpAction() = consume {
        val upIntent = if (parentView == ParentView.EVENT_DETAILS) {
            val eventDetailsRequest = NavigationRequest.EventDetails(parentEventId)
            EventDetailsActivity.createIntent(this, eventDetailsRequest)
        } else {
            MainActivity.createIntent(this)
        }
        upIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(upIntent)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}