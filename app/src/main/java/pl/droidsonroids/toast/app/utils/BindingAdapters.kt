@file:JvmName("BindingAdapters")

package pl.droidsonroids.toast.app.utils


import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.florent37.glidepalette.GlidePalette
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.utils.extensions.firstWord
import pl.droidsonroids.toast.app.utils.extensions.setImageColor
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.LoadingStatus
import java.text.SimpleDateFormat
import java.util.*


private const val COLOR_TRANSPARENT = 0x00FFFFFF

@BindingAdapter("eventTime")
fun TextView.setEventTime(date: Date?) {
    val timeFormatter = DateFormat.getTimeFormat(context)
    text = date?.let { timeFormatter.format(it) }
}


@BindingAdapter("eventDate")
fun TextView.setEventDate(date: Date?) {
    val timeFormatter = SimpleDateFormat(Constants.Date.PATTERN, Locale.getDefault())
    text = date?.let { timeFormatter.format(it) }
}

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

@BindingAdapter("gradientColor")
fun setGradientColor(imageSwitcher: ImageSwitcher, color: Int?) {
    color?.let {
        imageSwitcher.setImageDrawable(GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(color, COLOR_TRANSPARENT)))
    }
}

@BindingAdapter("loadingContainerVisibility")
fun setLoadingContainerVisibility(loadingContainer: View, loadingStatus: LoadingStatus) {
    loadingContainer.visibility = when (loadingStatus) {
        LoadingStatus.PENDING -> View.VISIBLE
        else -> View.GONE
    }
}


@BindingAdapter("loadingIndicatorVisibility")
fun setLoadingIndicatorVisibility(loadingContainer: View, loadingStatus: LoadingStatus) {
    loadingContainer.visibility = when (loadingStatus) {
        LoadingStatus.PENDING -> View.VISIBLE
        else -> View.INVISIBLE
    }
}

@BindingAdapter("connectionErrorContainerVisibility")
fun setConnectionErrorContainerVisibility(errorConnectionContainer: View, loadingStatus: LoadingStatus) {
    errorConnectionContainer.visibility = when (loadingStatus) {
        LoadingStatus.ERROR -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("transitionName", "elementId")
fun setTransitionName(view: View, transitionName: String, elementId: Long?) {
    view.transitionName = "$transitionName$elementId"
}

@BindingAdapter("about")
fun setAboutPrefix(textView: TextView, name: String) {
    textView.text = textView.context.getString(R.string.about, name.firstWord())
}

@BindingAdapter("setLinkEnabled")
fun setLinkImageButtonEnabled(imageButton: ImageButton, link: String?) {
    if (link.isNullOrEmpty()) {
        imageButton.setImageColor(R.color.disabledGray)
        imageButton.isEnabled = false
    } else {
        imageButton.setImageColor(R.color.colorPrimary)
        imageButton.isEnabled = true
    }
}