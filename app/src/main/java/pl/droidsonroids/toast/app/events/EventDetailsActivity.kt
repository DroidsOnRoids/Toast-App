package pl.droidsonroids.toast.app.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_event_details.*
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.databinding.ActivityEventDetailsBinding
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.event.EventDetailsViewModel

class EventDetailsActivity : BaseActivity() {
    companion object {
        private const val EVENT_ID = "event_id"

        fun createIntent(context: Context, eventDetailsRequest: NavigationRequest.EventDetails): Intent {
            return Intent(context, EventDetailsActivity::class.java)
                    .putExtra(EVENT_ID, eventDetailsRequest.id)
        }
    }

    private val eventId: Long by lazy {
        intent.getLongExtra(EVENT_ID, 0)
    }
    private val eventDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(eventId.toString(), EventDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val eventDetailsBinding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(eventDetailsBinding.root)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel(eventDetailsBinding)
        setupGradientSwitcher()
    }

    private fun setupViewModel(eventDetailsBinding: ActivityEventDetailsBinding) {
        eventDetailsViewModel.init(eventId)
        eventDetailsBinding.eventDetailsViewModel = eventDetailsViewModel
    }

    private fun setupGradientSwitcher() {
        gradientSwitcher.setFactory {
            ImageView(applicationContext).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            }
        }
    }
}