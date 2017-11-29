package pl.droidsonrioids.toast.viewmodels

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.data.model.Image
import java.text.SimpleDateFormat
import java.util.*


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("eventTime")
    fun setEventTime(textView: TextView, date: Date?) {
        val timeFormatter = SimpleDateFormat("hh:mm a zz", Locale.getDefault())
        if (date != null) {
            val formattedDate = timeFormatter.format(date)
            textView.text = formattedDate
        }
    }

    @JvmStatic
    @BindingAdapter("eventDate")
    fun setEventDate(textView: TextView, date: Date?) {
        val timeFormatter = SimpleDateFormat("dd.M.Y", Locale.getDefault())
        if (date != null) {
            val formattedDate = timeFormatter.format(date)
            textView.text = formattedDate
        }
    }

    @JvmStatic
    @BindingAdapter("eventCoverImage")
    fun setEventCoverImage(imageView: ImageView, imageUrlList: List<Image>?) {
        if (imageUrlList != null)
            Picasso.with(imageView.context)
                    .load(imageUrlList[1].big)
                    .placeholder(R.drawable.ic_placeholder_toast)
                    .into(imageView)
    }


}