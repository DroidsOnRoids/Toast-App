@file:JvmName("BindingAdapters")

package pl.droidsonrioids.toast.app.utils

import android.databinding.BindingAdapter
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import pl.droidsonrioids.toast.BuildConfig
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.dto.ImageDto
import pl.droidsonrioids.toast.utils.Consts
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
    // TODO: handle caching
    Picasso.Builder(imageView.context)
            .downloader(OkHttp3Downloader(OkHttpClient()))
            .build()
            .showIndicatorsIfDebug()
            .load(imageDto?.originalSizeUrl)
            .placeholder(R.drawable.ic_placeholder_toast)
            .fit()
            .centerCrop()
            .into(imageView)
}

private fun Picasso.showIndicatorsIfDebug(): Picasso {
    return apply {
        if (BuildConfig.DEBUG) {
            setIndicatorsEnabled(true)
        }
    }
}
