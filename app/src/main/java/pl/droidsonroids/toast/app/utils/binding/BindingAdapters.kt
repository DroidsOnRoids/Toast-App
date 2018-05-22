@file:JvmName("BindingAdapters")

package pl.droidsonroids.toast.app.utils.binding


import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.utils.extensions.firstWord
import pl.droidsonroids.toast.app.utils.extensions.setImageColor
import pl.droidsonroids.toast.data.enums.AttendStatus

@BindingAdapter("android:visibility")
fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("transitionName", "elementId")
fun setTransitionName(view: View, transitionName: String, elementId: Long?) {
    view.transitionName = "$transitionName$elementId"
}

@BindingAdapter("about")
fun setAboutPrefix(textView: TextView, name: String) {
    textView.text = textView.context.getString(R.string.about, name.firstWord())
}

@BindingAdapter(value = ["linkEnabled"], requireAll = false)
fun setLinkImageButtonEnabledWithColor(imageButton: ImageButton, link: String?) {
    imageButton.isEnabled = !link.isNullOrEmpty()
    when {
        imageButton.isEnabled.not() -> imageButton.setImageColor(R.color.disabledGray)
        imageButton.id == R.id.githubImage -> imageButton.setImageColor(R.color.black)
        imageButton.id == R.id.emailImage -> imageButton.setImageColor(R.color.redGmail)
        imageButton.id == R.id.twitterImage -> imageButton.setImageColor(R.color.blueTwitter)
        else -> imageButton.setImageColor(R.color.darkBlue)
    }
}

@BindingAdapter("isPastEvent", "android:text")
fun setAttendText(textView: TextView, isPastEvent: Boolean, attendStatus: AttendStatus?) {
    val text = when (attendStatus) {
        AttendStatus.ATTENDING -> if (isPastEvent) R.string.attended else R.string.attending
        AttendStatus.UNSURE -> R.string.interested_in
        AttendStatus.DECLINED, null -> R.string.attend
        AttendStatus.ERROR -> throw IllegalArgumentException("AttendStatus.ERROR is not allowed as text value")
    }
    textView.setText(text)
}

@BindingAdapter("android:foreground")
fun setForeground(cardView: CardView, isEnabled: Boolean) {
    val color = if (isEnabled) R.color.whiteAlpha60 else R.drawable.black_ripple
    with(cardView) {
        foreground = ContextCompat.getDrawable(context, color)
    }
}

@BindingAdapter("notificationScheduled")
fun ImageView.setNotificationIcon(isScheduled: Boolean) {
    val image = if (isScheduled) R.drawable.ic_notifications_on else R.drawable.ic_notifications_off
    setImageResource(image)
}