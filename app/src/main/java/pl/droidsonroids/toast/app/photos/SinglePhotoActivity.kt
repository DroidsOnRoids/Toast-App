package pl.droidsonroids.toast.app.photos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_single_photo.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.databinding.ItemSinglePhotoBinding
import pl.droidsonroids.toast.utils.NavigationRequest

class SinglePhotoActivity : BaseActivity() {
    companion object {
        private const val PHOTOS_KEY = "photo_key"
        private const val CURRENT_PHOTO_POSITION_KEY = "current_photo_position_key"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SinglePhoto): Intent {
            return Intent(context, SinglePhotoActivity::class.java)
                    .putExtra(PHOTOS_KEY, ArrayList(navigationRequest.photos))
                    .putExtra(CURRENT_PHOTO_POSITION_KEY, navigationRequest.position)
        }
    }

    private val photos by lazy {
        intent.getParcelableArrayListExtra<ImageDto>(PHOTOS_KEY)
    }

    private val currentPosition by lazy {
        intent.getIntExtra(CURRENT_PHOTO_POSITION_KEY, 0)
    }

    private val viewPagerPageMargin by lazy {
        resources.getDimensionPixelSize(R.dimen.margin_large)
    }

    private var isTransitionPostponed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_photo)
        supportPostponeEnterTransition()
        isTransitionPostponed = true
        setupViewPager()
    }

    private fun setupViewPager() {
        // TODO: 18/01/2018 Refactor
        with(photoViewPager) {
            adapter = PhotosViewPagerAdapter()
            currentItem = currentPosition
            pageMargin = viewPagerPageMargin
            offscreenPageLimit = 2
        }
    }

    inner class PhotosViewPagerAdapter : PagerAdapter() {

        override fun isViewFromObject(view: View, other: Any) = view == other

        override fun getCount() = photos.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater = LayoutInflater.from(container.context)
            val singlePhotoBinding = ItemSinglePhotoBinding.inflate(layoutInflater, container, false)
            singlePhotoBinding.setPhoto(photos[position])
            singlePhotoBinding.position = position.toLong()
            singlePhotoBinding.imageLoaded = {
                if (currentPosition == position && isTransitionPostponed) {
                    supportStartPostponedEnterTransition()
                    isTransitionPostponed = false
                }
            }
            container.addView(singlePhotoBinding.root)
            return singlePhotoBinding.root
        }

        override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
            container.removeView(view as View)
        }

    }

}
