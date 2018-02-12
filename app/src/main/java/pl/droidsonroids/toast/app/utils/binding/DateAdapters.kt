package pl.droidsonroids.toast.app.utils.binding

import android.databinding.BindingAdapter
import android.text.format.DateFormat
import android.widget.TextView
import pl.droidsonroids.toast.utils.Constants
import java.text.SimpleDateFormat
import java.util.*


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
