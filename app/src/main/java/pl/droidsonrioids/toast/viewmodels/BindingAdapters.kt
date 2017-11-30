package pl.droidsonrioids.toast.viewmodels

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.model.Image
import java.text.SimpleDateFormat
import java.util.*


object BindingAdapters {
    private const val TIME_PATTERN = "hh:mm a zz"
    private const val DATE_PATTERN = "dd.M.Y"
    private const val FIRST_COVER_INDEX = 0

    @JvmStatic
    @BindingAdapter("eventTime")
    fun setEventTime(textView: TextView, date: Date?) {
        val timeFormatter = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
        if (date != null) {
            val formattedDate = timeFormatter.format(date)
            textView.text = formattedDate
        }
    }

    @JvmStatic
    @BindingAdapter("eventDate")
    fun setEventDate(textView: TextView, date: Date?) {
        val timeFormatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        if (date != null) {
            val formattedDate = timeFormatter.format(date)
            textView.text = formattedDate
        }
    }

    @JvmStatic
    @BindingAdapter("eventCoverImage")
    fun setEventCoverImage(imageView: ImageView, imageUrlList: List<Image>?) {
        if (imageUrlList != null) {
            Picasso.Builder(imageView.context)
                    .downloader(OkHttp3Downloader(OkHttpClient()))
                    .build().load(imageUrlList[FIRST_COVER_INDEX].big)
                    .placeholder(R.drawable.ic_placeholder_toast)
                    .fit()
                    .centerCrop()
                    .into(imageView)
        }
    }

}