package pl.droidsonroids.toast.app.events

import android.content.Context
import android.content.Intent
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.utils.Constants.EVENT_ID
import pl.droidsonroids.toast.utils.NavigationRequest

class EventDetailsActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context, eventDetailsRequest: NavigationRequest.EventDetails): Intent {
            return Intent(context, EventDetailsActivity::class.java).apply {
                putExtra(EVENT_ID, eventDetailsRequest.id)
            }
        }
    }
}