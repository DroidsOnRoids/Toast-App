package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_speakers_search.*
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.events.EventDetailsActivity
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.app.utils.ParentView
import pl.droidsonroids.toast.databinding.ActivitySpeakerDetailsBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerDetailsViewModel

class SpeakerDetailsActivity : BaseActivity() {
    companion object {
        private const val SPEAKER_ID: String = "speaker_id"
        private const val PARENT_VIEW_KEY = "parent_view_key"
        private const val EVENT_ID_KEY = "parent_event_id"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SpeakerDetails): Intent {
            return Intent(context, SpeakerDetailsActivity::class.java)
                    .putExtra(SPEAKER_ID, navigationRequest.id)
                    .putExtra(PARENT_VIEW_KEY, navigationRequest.parentView)
                    .putExtra(EVENT_ID_KEY, navigationRequest.eventId)
        }
    }

    private val speakerDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(speakerId.toString(), SpeakerDetailsViewModel::class.java)
    }

    private val speakerId: Long by lazy {
        intent.getLongExtra(SPEAKER_ID, Constants.NO_ID)
    }

    private val parentEventId by lazy {
        intent.getLongExtra(EVENT_ID_KEY, Constants.NO_ID)
    }

    private val parentView by lazy {
        intent.getSerializableExtra(PARENT_VIEW_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val speakerDetailsBinding = ActivitySpeakerDetailsBinding.inflate(layoutInflater)
        setContentView(speakerDetailsBinding.root)
        setupViewModel(speakerDetailsBinding)
        setupToolbar()
        setupViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> consume {
                if (isTaskRoot)
                    handleUpAction()
                else
                    finish()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot)
            handleUpAction()
        else
            super.onBackPressed()
    }

    private fun setupViewModel(speakerDetailsBinding: ActivitySpeakerDetailsBinding) {
        speakerDetailsBinding.speakerDetailsViewModel = speakerDetailsViewModel
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun handleUpAction() {

        val upIntent = when (parentView) {
            ParentView.EVENT_DETAILS -> EventDetailsActivity.createIntent(this, NavigationRequest.EventDetails(parentEventId))
            ParentView.SPEAKERS_SEARCH -> SpeakersSearchActivity.createIntent(this)
            else -> MainActivity.createIntent(this)
        }
        upIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(upIntent)
    }

    private fun setupViewModel() {
        speakerDetailsViewModel.init(speakerId)
    }

}