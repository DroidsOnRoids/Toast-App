@file:JvmName("BindingAdapters")

package pl.droidsonrioids.toast.app.utils

import android.databinding.BindingAdapter
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.dto.ImageDto
import pl.droidsonrioids.toast.utils.Consts
import pl.droidsonrioids.toast.utils.LoadingStatus
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("eventTime")
fun setEventTime(textView: TextView, date: Date?) {
    val timeFormatter = DateFormat.getTimeFormat(textView.context)
    textView.text = date?.let { timeFormatter.format(it) }
}


@BindingAdapter("eventDate")
fun setEventDate(textView: TextView, date: Date?) {
    val timeFormatter = SimpleDateFormat(Consts.DATE_PATTERN, Locale.getDefault())
    textView.text = date?.let { timeFormatter.format(it) }
}


@BindingAdapter("eventCoverImage")
fun setEventCoverImage(imageView: ImageView, imageDto: ImageDto?) {
    val thumbnailLoader = Glide.with(imageView).load(imageDto?.thumbSizeUrl)
    Glide.with(imageView)
            .load(imageDto?.originalSizeUrl)
            .thumbnail(thumbnailLoader)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder_toast))
            .into(imageView)
}

@BindingAdapter("loadingContainerVisibility")
fun setEventsLoadingProgressBarVisibility(loadingContainer: View, loadingStatus: LoadingStatus) {
    when (loadingStatus) {
        LoadingStatus.PENDING -> loadingContainer.visibility = View.VISIBLE
        else -> loadingContainer.visibility = View.GONE
    }
}
