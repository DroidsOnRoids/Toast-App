package pl.droidsonrioids.toast.viewmodels

import android.databinding.BindingAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


object BindAdapters {

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
}