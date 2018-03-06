package pl.droidsonroids.toast.app.utils.binding

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageSwitcher
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
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


@BindingAdapter("originalImage")
fun setOriginalImage(imageView: ImageView, imageDto: ImageDto?) {
    val thumbnailLoader = Glide.with(imageView).load(imageDto?.thumbSizeUrl)
    val requestOptions = RequestOptions.placeholderOf(R.drawable.ic_placeholder_toast)
            .override(Target.SIZE_ORIGINAL)
    Glide.with(imageView)
            .load(imageDto?.originalSizeUrl)
            .thumbnail(thumbnailLoader)
            .apply(requestOptions)
            .into(imageView)
}

@SuppressLint("CheckResult")
@BindingAdapter("originalImage", "loadingFinishedListener", "fromCache")
fun loadOriginalPhoto(imageView: ImageView, imageDto: ImageDto?, onLoadingFinished: () -> Unit, loadFromCache: Boolean) {
    val listener = createRequestListener(onLoadingFinished)
    loadWithListener(imageView, imageDto, listener, isRoundImage = false) {
        apply(RequestOptions().override(Target.SIZE_ORIGINAL).onlyRetrieveFromCache(loadFromCache))
    }
}

@SuppressLint("CheckResult")
@BindingAdapter("roundImage", "loadingFinishedListener", "fromCache")
fun loadRoundPhoto(imageView: ImageView, imageDto: ImageDto?, onLoadingFinished: () -> Unit, loadFromCache: Boolean) {
    val listener = createRequestListener(onLoadingFinished)
    loadWithListener(imageView, imageDto, listener, isRoundImage = true) {
        apply(RequestOptions().override(Target.SIZE_ORIGINAL).onlyRetrieveFromCache(loadFromCache))
    }
}

private fun createRequestListener(onLoadingFinished: () -> Unit): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }
    }
}

@SuppressLint("CheckResult")
@BindingAdapter("originalImage", "imageColorListener", "loadingFinishedListener", "fromCache")
fun setCoverImageWithPaletteListener(imageView: ImageView, imageDto: ImageDto?, onColorLoaded: (Int) -> Unit, onLoadingFinished: () -> Unit, loadFromCache: Boolean) {
    val listener = createGlidePaletteListener(imageDto, onColorLoaded, onLoadingFinished)
    loadWithListener(imageView, imageDto, listener, isRoundImage = false) {
        apply(RequestOptions().override(Target.SIZE_ORIGINAL).onlyRetrieveFromCache(loadFromCache))
    }
}

private fun loadWithListener(imageView: ImageView, imageDto: ImageDto?, listener: RequestListener<Drawable>, isRoundImage: Boolean, apply: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }) {
    val thumbnailLoader = (Glide.with(imageView))
            .load(imageDto?.thumbSizeUrl)
            .apply {
                if (isRoundImage) apply(RequestOptions.circleCropTransform())
            }
    Glide.with(imageView)
            .load(imageDto?.originalSizeUrl)
            .thumbnail(thumbnailLoader)
            .listener(listener)
            .apply()
            .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder_toast)
                    .apply {
                        if (isRoundImage) circleCrop()
                    })
            .into(imageView)
}

private fun createGlidePaletteListener(imageDto: ImageDto?, onColorLoaded: (Int) -> Unit, onLoadingFinished: () -> Unit): GlidePalette<Drawable> {
    return GlidePalette.with(imageDto?.originalSizeUrl)
            .setGlideListener(createRequestListener(onLoadingFinished))
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