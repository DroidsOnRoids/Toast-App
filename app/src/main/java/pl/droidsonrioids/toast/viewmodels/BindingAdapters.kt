@file:JvmName("BindingAdapters")

package pl.droidsonrioids.toast.viewmodels

import android.databinding.BindingAdapter
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import pl.droidsonrioids.toast.BuildConfig
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.model.Image
import pl.droidsonrioids.toast.data.model.LoadingStatus
import java.text.SimpleDateFormat
import java.util.*


const val DATE_PATTERN = "dd.MM.yyyy"
private const val FIRST_COVER_INDEX = 0


@BindingAdapter("eventTime")
fun setEventTime(textView: TextView, date: Date?) {
    val timeFormatter = DateFormat.getTimeFormat(textView.context)
    textView.text = date?.let { timeFormatter.format(it) }
}


@BindingAdapter("eventDate")
fun setEventDate(textView: TextView, date: Date?) {
    val timeFormatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    textView.text = date?.let { timeFormatter.format(it) }
}


@BindingAdapter("eventCoverImage")
fun setEventCoverImage(imageView: ImageView, imageUrlList: List<Image>?) {
    // TODO: handle caching
    if (imageUrlList != null) {
        Picasso.Builder(imageView.context)
                .downloader(OkHttp3Downloader(OkHttpClient()))
                .build()
                .showIndicatorsIfDebug()
                .load(imageUrlList[FIRST_COVER_INDEX].big)
                .placeholder(R.drawable.ic_placeholder_toast)
                .fit()
                .centerCrop()
                .into(imageView)
    }
}

@BindingAdapter("eventsProgressBarVisibility")
fun setEventsLoadingProgressBarVisibility(progressBar: ProgressBar, loadingStatus: LoadingStatus) {
    when (loadingStatus) {
        LoadingStatus.PENDING -> progressBar.visibility = View.VISIBLE
        else -> progressBar.visibility = View.GONE
    }
}

@BindingAdapter("eventsContentVisibility")
fun setEventsContentVisibility(eventsContent: View, loadingStatus: LoadingStatus) {
    when (loadingStatus) {
        LoadingStatus.SUCCESS -> eventsContent.visibility = View.VISIBLE
        else -> eventsContent.visibility = View.GONE
    }
}

@BindingAdapter("errorConnectionVisibility")
fun setErrorConnectionVisibility(errorConnectionContent: View, loadingStatus: LoadingStatus) {
    when (loadingStatus) {
        LoadingStatus.ERROR -> errorConnectionContent.visibility = View.VISIBLE
        else -> errorConnectionContent.visibility = View.GONE
    }
}

private fun Picasso.showIndicatorsIfDebug(): Picasso {
    return apply {
        if (BuildConfig.DEBUG) {
            setIndicatorsEnabled(true)
        }
    }
}
