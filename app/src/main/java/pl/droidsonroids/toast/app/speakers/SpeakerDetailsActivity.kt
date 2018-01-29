package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_speaker_details.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.databinding.ActivitySpeakerDetailsBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerDetailsViewModel

class SpeakerDetailsActivity : BaseActivity() {
    companion object {
        private const val SPEAKER_ID: String = "speaker_id"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SpeakerDetails): Intent {
            return Intent(context, SpeakerDetailsActivity::class.java)
                    .putExtra(SPEAKER_ID, navigationRequest.id)
        }
    }

    private val speakerDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(speakerId.toString(), SpeakerDetailsViewModel::class.java)
    }

    private val speakerId: Long by lazy {
        intent.getLongExtra(SPEAKER_ID, Constants.NO_ID)
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
            android.R.id.home -> consume { finish() }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViewModel(speakerDetailsBinding: ActivitySpeakerDetailsBinding) {
        speakerDetailsBinding.speakerDetailsViewModel = speakerDetailsViewModel
    }

    private fun setupToolbar() {
        val contentScrimColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val statusBarScrimColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            setToolbarScrim(verticalOffset, appBarLayout, contentScrimColor, statusBarScrimColor)
        }
        collapsingToolbar.scrimVisibleHeightTrigger = Int.MAX_VALUE
    }

    private fun setToolbarScrim(verticalOffset: Int, appBarLayout: AppBarLayout, contentScrimColor: Int, statusBarScrimColor: Int) {
        val offsetFraction = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
        val alphaValue = (offsetFraction * 255).toInt()
        val contentScrimWithAlpha = ColorUtils.setAlphaComponent(contentScrimColor, alphaValue)
        val statusBarScrimWithAlpha = ColorUtils.setAlphaComponent(statusBarScrimColor, alphaValue)

        collapsingToolbar.setContentScrimColor(contentScrimWithAlpha)
        collapsingToolbar.setStatusBarScrimColor(statusBarScrimWithAlpha)
    }

    private fun setupViewModel() {
        speakerDetailsViewModel.init(speakerId)
    }

}