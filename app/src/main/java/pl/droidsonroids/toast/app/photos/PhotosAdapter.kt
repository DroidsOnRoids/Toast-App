package pl.droidsonroids.toast.app.photos

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonroids.toast.databinding.ItemPhotoBinding
import pl.droidsonroids.toast.viewmodels.photos.PhotoItemViewModel

class PhotosAdapter : RecyclerView.Adapter<PhotoItemViewHolder>() {
    private var photoItemViewModels = emptyList<PhotoItemViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        holder.bind(photoItemViewModels[position])
    }

    override fun getItemCount() = photoItemViewModels.size

    fun setData(newPhotoItemViewModels: List<PhotoItemViewModel>) {
        photoItemViewModels = newPhotoItemViewModels
        notifyDataSetChanged()
    }
}

class PhotoItemViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photoItemViewModel: PhotoItemViewModel) {
        binding.photoItemViewModel = photoItemViewModel
        binding.executePendingBindings()
    }
}
