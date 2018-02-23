package pl.droidsonroids.toast.app.photos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
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
import kotlin.math.abs

class PhotosActivity : BaseActivity() {
    companion object {
        private const val PHOTOS_KEY = "photos_key"
        private const val EVENT_ID_KEY = "event_id_key"
        private const val PARENT_VIEW_KEY = "parent_view_key"
        private const val PHOTOS_GRID_SIZE = 2

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

    private lateinit var listAnimator: ViewsTransitionAnimator<Int>
    private lateinit var pagerAdapter: PhotosViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        setupWindow()
        setupToolbar()
        setupViewModel()
        setupRecyclerView()
        setupViewPager()
        initPagerAnimator()
        increaseGlideMemoryCache()
    }

    override fun onBackPressed() {
        if (!listAnimator.isLeaving) {
            listAnimator.exit(true)
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

    private fun initPagerAnimator() {
        val gridTracker = object : SimpleTracker() {
            public override fun getViewAt(position: Int): View? {
                return photosRecyclerView.findViewHolderForLayoutPosition(position).itemView
            }
        }

        val pagerTracker = object : SimpleTracker() {
            public override fun getViewAt(position: Int): View? {
                val holder = pagerAdapter.getViewHolder(position)
                return if (holder == null) null else pagerAdapter.getImage(holder)
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

        photosAppBar.alpha = abs(1 - position)

        if (isLeaving && position == 0f) {
            showSystemUi(true)
        }
    }

    private fun showSystemUi(show: Boolean) {
        val flags = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)

        window.decorView.systemUiVisibility = if (show) 0 else flags
    }

    private fun increaseGlideMemoryCache() {
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)
    }

    private fun setupWindow() {
        window.sharedElementsUseOverlay = false
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
        if (navigationRequest is NavigationRequest.SinglePhoto) {
            navigator.showSinglePhotoWithSharedAnimation(this, navigationRequest, getSharedViews(navigationRequest.position.toInt()))
        } else {
            navigator.dispatch(this, navigationRequest)
        }
    }

    private fun getSharedViews(position: Int): Array<Pair<View, String>> {
        return photosRecyclerView.findViewHolderForAdapterPosition(position)
                ?.itemView
                ?.run {
                    val photoView = findViewById<View>(R.id.photo)
                    arrayOf(Pair(photoView, photoView.transitionName))
                } ?: emptyArray()
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