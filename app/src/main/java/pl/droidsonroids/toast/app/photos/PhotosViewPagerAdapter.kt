package pl.droidsonroids.toast.app.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.alexvasilkov.gestures.views.GestureImageView
import pl.droidsonroids.toast.R
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

    override fun getCount() = singlePhotoViewModels.size

    inner class FullPhotoViewHolder(private val binding: ItemSinglePhotoBinding) : RecyclePagerAdapter.ViewHolder(binding.root) {
        fun bind(singlePhotoViewModel: SinglePhotoViewModel) {
            binding.singlePhotoViewModel = singlePhotoViewModel
            binding.executePendingBindings()
        }
    }

    fun getImage(holder: FullPhotoViewHolder): GestureImageView {
        return holder.itemView.findViewById(R.id.photo)
    }

}