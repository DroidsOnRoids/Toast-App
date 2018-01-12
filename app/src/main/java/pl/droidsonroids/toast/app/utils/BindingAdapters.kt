@file:JvmName("BindingAdapters")

package pl.droidsonroids.toast.app.utils


import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.design.widget.TextInputLayout
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.glidepalette.GlidePalette
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.LoadingStatus
import java.text.SimpleDateFormat
import java.util.*


private const val COLOR_TRANSPARENT = 0x00FFFFFF

@BindingAdapter("eventTime")
fun setEventTime(textView: TextView, date: Date?) {
    val timeFormatter = DateFormat.getTimeFormat(textView.context)
    textView.text = date?.let { timeFormatter.format(it) }
}


@BindingAdapter("eventDate")
fun setEventDate(textView: TextView, date: Date?) {
    val timeFormatter = SimpleDateFormat(Constants.Date.PATTERN, Locale.getDefault())
    textView.text = date?.let { timeFormatter.format(it) }
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

@BindingAdapter("coverImage", "coverImageColorListener")
fun setCoverImageWithPaletteListener(imageView: ImageView, imageDto: ImageDto?, onColorLoaded: (Int) -> Unit) {
    val thumbnailLoader = Glide.with(imageView)
            .load(imageDto?.thumbSizeUrl)
    Glide.with(imageView)
            .load(imageDto?.originalSizeUrl)
            .thumbnail(thumbnailLoader)
            .listener(createGlidePaletteListener(imageDto, onColorLoaded))
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

@BindingAdapter("app:errorText")
fun setInputErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage
}