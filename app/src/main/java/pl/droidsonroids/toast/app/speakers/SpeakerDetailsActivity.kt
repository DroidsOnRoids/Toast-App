package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_speaker_details.*
import io.reactivex.disposables.Disposables
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.databinding.ActivitySpeakerDetailsBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerDetailsViewModel
import kotlin.math.abs
import javax.inject.Inject

class SpeakerDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        private const val SPEAKER_ID: String = "speaker_id"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SpeakerDetails): Intent {
            return Intent(context, SpeakerDetailsActivity::class.java)
                    .putExtra(SPEAKER_ID, navigationRequest.id)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private var navigationDisposable = Disposables.disposed()

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

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (abs(verticalOffset) == abs(appBar.height - toolbar.height)) {
            toolbarAvatarImage.visibility = View.VISIBLE
        } else {
            toolbarAvatarImage.visibility = View.INVISIBLE
        }
    }

    private fun setupViewModel(speakerDetailsBinding: ActivitySpeakerDetailsBinding) {
        speakerDetailsBinding.speakerDetailsViewModel = speakerDetailsViewModel
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appBar.addOnOffsetChangedListener(this)
    }

    private fun setupViewModel() {
        speakerDetailsViewModel.init(speakerId)
        navigationDisposable = speakerDetailsViewModel.navigationSubject
                .subscribe {
                    navigator.dispatch(context = this, navigationRequest = it)
                }
    }

    override fun onDestroy() {
        navigationDisposable.dispose()
        super.onDestroy()
    }

}