package pl.droidsonroids.toast.app.utils.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageSwitcher
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.florent37.glidepalette.GlidePalette
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.utils.SortingType

private const val COLOR_TRANSPARENT = 0x00FFFFFF

@BindingAdapter("coverImage")
fun setCoverImage(imageView: ImageView, imageDto: ImageDto?) {
    val thumbnailLoader = Glide.with(imageView).load(imageDto?.thumbSizeUrl)
    Glide.with(imageView)
            .load(imageDto?.originalSizeUrl)
            .thumbnail(thumbnailLoader)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder_toast))
            .into(imageView)
}

@BindingAdapter("roundImage")
fun setRoundImage(imageView: ImageView, imageDto: ImageDto?) {
    val thumbnailLoader = Glide.with(imageView)
            .load(imageDto?.thumbSizeUrl)
            .apply(RequestOptions.circleCropTransform())

    Glide.with(imageView)
            .load(imageDto?.originalSizeUrl)
            .thumbnail(thumbnailLoader)
            .apply(RequestOptions
                    .placeholderOf(R.drawable.ic_placeholder_toast)
                    .circleCrop())
            .into(imageView)
}

@BindingAdapter("coverImage", "loadingFinishedListener")
fun setCoverImageWithLoadedListener(imageView: ImageView, imageDto: ImageDto?, onLoadingFinished: () -> Unit) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }
    }
    loadWithListener(imageView, imageDto, listener)
}

@BindingAdapter("coverImage", "coverImageColorListener")
fun setCoverImageWithPaletteListener(imageView: ImageView, imageDto: ImageDto?, onColorLoaded: (Int) -> Unit) {
    val listener = createGlidePaletteListener(imageDto, onColorLoaded)
    loadWithListener(imageView, imageDto, listener)
}

private fun loadWithListener(imageView: ImageView, imageDto: ImageDto?, listener: RequestListener<Drawable>) {
    val thumbnailLoader = Glide.with(imageView)
            .load(imageDto?.thumbSizeUrl)
    Glide.with(imageView)
            .load(imageDto?.originalSizeUrl)
            .thumbnail(thumbnailLoader)
            .listener(listener)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder_toast))
            .into(imageView)
}

private fun createGlidePaletteListener(imageDto: ImageDto?, onColorLoaded: (Int) -> Unit): GlidePalette<Drawable> {
    return GlidePalette.with(imageDto?.originalSizeUrl)
            .intoCallBack { palette ->
                val darkVibrantColor = palette?.darkVibrantSwatch?.rgb
                darkVibrantColor?.let { onColorLoaded(it) }
            }
}

@BindingAdapter("android:src")
fun ImageView.setSortingImage(sortingType: SortingType) {
    if (sortingType == SortingType.ALPHABETICAL) {
        setImageResource(R.drawable.ic_sorting_alphabetical)
    } else {
        setImageResource(R.drawable.ic_sorting_by_date)
    }
}

@BindingAdapter("gradientColor")
fun setGradientColor(imageSwitcher: ImageSwitcher, color: Int?) {
    color?.let {
        imageSwitcher.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(color, COLOR_TRANSPARENT)))
    }
}