package pl.droidsonroids.toast.app.photos

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.droidsonroids.toast.databinding.ItemSinglePhotoBinding
import pl.droidsonroids.toast.viewmodels.photos.SinglePhotoViewModel

class PhotosViewPagerAdapter(private val singlePhotoViewModels: List<SinglePhotoViewModel>) : PagerAdapter() {

    override fun isViewFromObject(view: View, other: Any) = view == other

    override fun getCount() = singlePhotoViewModels.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val singlePhotoBinding = ItemSinglePhotoBinding.inflate(layoutInflater, container, false)
        singlePhotoBinding.singlePhotoViewModel = singlePhotoViewModels[position]
        container.addView(singlePhotoBinding.root)
        return singlePhotoBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

}