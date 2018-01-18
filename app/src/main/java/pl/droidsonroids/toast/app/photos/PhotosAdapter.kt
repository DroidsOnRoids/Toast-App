package pl.droidsonroids.toast.app.photos

import android.support.v7.util.DiffUtil
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
        val diffCallback = PhotosItemDiffCallback(photoItemViewModels, newPhotoItemViewModels)
        val diff = DiffUtil.calculateDiff(diffCallback)
        photoItemViewModels = newPhotoItemViewModels
        diff.dispatchUpdatesTo(this)
    }

    class PhotosItemDiffCallback(
            private val oldList: List<PhotoItemViewModel>,
            private val newList: List<PhotoItemViewModel>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].image == newList[newItemPosition].image
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

