package pl.droidsonroids.toast.app.photos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        setupWindow()
        setupToolbar()
        setupViewModel()
        setupRecyclerView()
        increaseGlideMemoryCache()
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
        photosViewModel.init(photos)
        compositeDisposable += photosViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
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
        photosViewModel.sharedTransitionInProgress = false
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