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
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_photos.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.home.MainActivity
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
    private lateinit var pagerAdapter: PhotosViewPagerAdapter
    private var defaultNavigationBarColor: Int = 0
    private var defaultStatusBarColor: Int = 0

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

    override fun onBackPressed() {
        if (!listAnimator.isLeaving) {
            listAnimator.exit(true)
            if (isImmersiveMode) {
                toggleImmersiveMode()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun setupViewPager() {
        pagerAdapter = PhotosViewPagerAdapter(photosViewModel.fullPhotosSubject.value)
        fullPhotoViewPager.adapter = pagerAdapter
        fullPhotoViewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.margin_large)
        pagerAdapter.notifyDataSetChanged()
    }

    private fun setupPagerAnimator() {
        val gridTracker = object : SimpleTracker() {
            public override fun getViewAt(position: Int): View? {
                return photosRecyclerView.findViewHolderForLayoutPosition(position).itemView
            }
        }

        val pagerTracker = object : SimpleTracker() {
            public override fun getViewAt(position: Int): View? {
                val holder = pagerAdapter.getViewHolder(position)
                return if (holder == null) null else pagerAdapter.getPhotoView(holder)
            }
        }

        listAnimator = GestureTransitions.from<Int>(photosRecyclerView, gridTracker)
                .into(fullPhotoViewPager, pagerTracker)

        listAnimator.addPositionUpdateListener(::applyFullPagerState)
    }


    private fun applyFullPagerState(position: Float, isLeaving: Boolean) {
        fullPhotoBackground.visibility = if (position == 0f) View.INVISIBLE else View.VISIBLE
        fullPhotoBackground.alpha = position

        fullPhotoToolbar.visibility = if (position == 0f) View.INVISIBLE else View.VISIBLE
        fullPhotoToolbar.alpha = position

        if (isLeaving && position == 0f && isImmersiveMode) {
            toggleImmersiveMode()
        }
    }

    private fun setupWindow() {
        defaultNavigationBarColor = window.navigationBarColor
        defaultStatusBarColor = window.statusBarColor
        window.sharedElementsUseOverlay = false
        window.decorView.systemUiVisibility = PhotosActivity.NORMAL_MODE
        ViewCompat.setOnApplyWindowInsetsListener(fullPhotoViewPager) { _, insets ->
            for (i in 0 until fullPhotoViewPager.childCount) {
                ViewCompat.dispatchApplyWindowInsets(fullPhotoViewPager.getChildAt(i), insets)
            }
            insets
        }
    }

    private fun toggleImmersiveMode() {
        window.decorView.systemUiVisibility = if (isImmersiveMode) PhotosActivity.NORMAL_MODE else PhotosActivity.IMMERSIVE_MODE
        val appBarTranslation = if (isImmersiveMode) -fullPhotoToolbar.bottom.toFloat() else 0.0f
        fullPhotoToolbar.animate().translationY(appBarTranslation).start()
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

    override fun onResume() {
        super.onResume()
        photosViewModel.isSharedTransitionInProgress = false
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
        setDefaultGlideMemoryCache()
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun setDefaultGlideMemoryCache() {
        Glide.get(this).setMemoryCategory(MemoryCategory.NORMAL)
    }
}