package pl.droidsonroids.toast.app.photos

import android.support.v7.widget.RecyclerView
import pl.droidsonroids.toast.databinding.ItemPhotoBinding
import pl.droidsonroids.toast.viewmodels.photos.PhotoItemViewModel

class PhotoItemViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photoItemViewModel: PhotoItemViewModel) {
        binding.photoItemViewModel = photoItemViewModel
        binding.position = adapterPosition.toLong()
        binding.executePendingBindings()
    }
}