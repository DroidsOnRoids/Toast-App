package pl.droidsonroids.toast.app.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.FrameLayout
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
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

    private var eventSpeakersDisposable = Disposables.disposed()

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
        setupRecyclerView()
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

    private fun setupRecyclerView() {
        with(eventSpeakersRecyclerView) {
            val eventSpeakersAdapter = EventSpeakersAdapter()
            adapter = eventSpeakersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            subscribeToSpeakersChange(eventSpeakersAdapter)
        }
    }

    private fun subscribeToSpeakersChange(eventSpeakersAdapter: EventSpeakersAdapter) {
        eventSpeakersDisposable = eventDetailsViewModel.eventSpeakers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    eventSpeakersAdapter.setData(it)
                }
    }

    override fun onDestroy() {
        eventSpeakersDisposable.dispose()
        super.onDestroy()
    }
}

