package pl.droidsonroids.toast.app.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_single_photo.view.*
import pl.droidsonroids.toast.databinding.ItemSinglePhotoBinding
import pl.droidsonroids.toast.viewmodels.photos.SinglePhotoViewModel

class PhotosViewPagerAdapter(private val singlePhotoViewModels: List<SinglePhotoViewModel>) : RecyclePagerAdapter<PhotosViewPagerAdapter.FullPhotoViewHolder>() {


    override fun onCreateViewHolder(container: ViewGroup): FullPhotoViewHolder {
        val singlePhotoBinding = ItemSinglePhotoBinding.inflate(LayoutInflater.from(container.context), container, false)
        return FullPhotoViewHolder(singlePhotoBinding)
    }

    override fun onBindViewHolder(holder: FullPhotoViewHolder, position: Int) {
        holder.bind(singlePhotoViewModels[position])
    }

    override fun onRecycleViewHolder(holder: FullPhotoViewHolder) {
        super.onRecycleViewHolder(holder)
        with(holder.itemView.photo) {
            Glide.with(this).clear(this)
            setImageDrawable(null)
        }
    }

    override fun getCount() = singlePhotoViewModels.size

    inner class FullPhotoViewHolder(private val binding: ItemSinglePhotoBinding) : RecyclePagerAdapter.ViewHolder(binding.root) {
        fun bind(singlePhotoViewModel: SinglePhotoViewModel) {
            binding.singlePhotoViewModel = singlePhotoViewModel
            binding.executePendingBindings()
        }
    }

    fun getPhotoView(holder: FullPhotoViewHolder): GestureImageView = holder.itemView.photo

}