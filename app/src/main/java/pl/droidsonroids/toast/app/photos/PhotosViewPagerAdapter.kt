package pl.droidsonroids.toast.app.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_fullscreen_photo.view.*
import pl.droidsonroids.toast.databinding.ItemFullscreenPhotoBinding
import pl.droidsonroids.toast.viewmodels.photos.FullscreenPhotoViewModel

class PhotosViewPagerAdapter(private var fullscreenPhotoViewModels: List<FullscreenPhotoViewModel>) : RecyclePagerAdapter<PhotosViewPagerAdapter.FullPhotoViewHolder>() {


    override fun onCreateViewHolder(container: ViewGroup): FullPhotoViewHolder {
        val singlePhotoBinding = ItemFullscreenPhotoBinding.inflate(LayoutInflater.from(container.context), container, false)
        return FullPhotoViewHolder(singlePhotoBinding)
    }

    override fun onBindViewHolder(holder: FullPhotoViewHolder, position: Int) {
        holder.bind(fullscreenPhotoViewModels[position])
    }

    override fun onRecycleViewHolder(holder: FullPhotoViewHolder) {
        super.onRecycleViewHolder(holder)
        with(holder.itemView.photo) {
            Glide.with(this).clear(this)
            setImageDrawable(null)
        }
    }

    override fun getCount() = fullscreenPhotoViewModels.size

    fun getPhotoView(holder: FullPhotoViewHolder): GestureImageView = holder.itemView.photo

    fun setData(newFullscreenPhotoViewModels: List<FullscreenPhotoViewModel>) {
        fullscreenPhotoViewModels = newFullscreenPhotoViewModels
        notifyDataSetChanged()
    }

    inner class FullPhotoViewHolder(private val binding: ItemFullscreenPhotoBinding) : RecyclePagerAdapter.ViewHolder(binding.root) {
        fun bind(fullscreenPhotoViewModel: FullscreenPhotoViewModel) {
            binding.fullscreenPhotoViewModel = fullscreenPhotoViewModel
            binding.executePendingBindings()
        }
    }


}