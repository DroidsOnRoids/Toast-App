@file:JvmName("BindingAdapters")

package pl.droidsonrioids.toast.viewmodels

import android.databinding.BindingAdapter
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import pl.droidsonrioids.toast.BuildConfig
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.model.Image
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
        val url = imageUrlList[FIRST_COVER_INDEX].big.addBaseUrlIfNeeded()
        Picasso.Builder(imageView.context)
                .downloader(OkHttp3Downloader(OkHttpClient()))
                .build()
                .showIndicatorsIfDebug()
                .load(url)
                .placeholder(R.drawable.ic_placeholder_toast)
                .fit()
                .centerCrop()
                .into(imageView)
    }
}

private fun Picasso.showIndicatorsIfDebug(): Picasso {
    return apply {
        if (BuildConfig.DEBUG) {
            setIndicatorsEnabled(true)
        }
    }
}
private fun String.addBaseUrlIfNeeded(): String {
    // TODO: Investigate with backend
    return if (startsWith("http", true)) {
        this
    } else BuildConfig.BASE_API_URL.removeSuffix("/api/v1/") + this
}
