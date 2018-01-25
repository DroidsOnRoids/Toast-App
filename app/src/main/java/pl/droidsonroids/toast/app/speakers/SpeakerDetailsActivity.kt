package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_speaker_details.*
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.databinding.ActivitySpeakerDetailsBinding
import pl.droidsonroids.toast.databinding.ItemSpeakerTalkBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.consume
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerDetailsViewModel
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerTalkViewModel

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

    private var talksDisposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val speakerDetailsBinding = ActivitySpeakerDetailsBinding.inflate(layoutInflater)
        setContentView(speakerDetailsBinding.root)
        setupToolbar()
        setupViewModel(speakerDetailsBinding)
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel(speakerDetailsBinding: ActivitySpeakerDetailsBinding) {
        speakerDetailsViewModel.init(speakerId)
        speakerDetailsBinding.speakerDetailsViewModel = speakerDetailsViewModel
    }

    private fun setupRecyclerView() {
        with(talksRecyclerView) {
            val talksAdapter = SpeakerTalksAdapter()
            adapter = talksAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            subscribeToTalksChanges(talksAdapter)
        }
    }

    private fun subscribeToTalksChanges(talksAdapter: SpeakerTalksAdapter) {
        talksDisposable = speakerDetailsViewModel.talksSubject
                .subscribe { talksAdapter.setData(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> consume { finish() }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        talksDisposable.dispose()
        super.onDestroy()
    }
}

class SpeakerTalksAdapter : RecyclerView.Adapter<SpeakerTalkViewHolder>() {
    private var speakerTalkViewModels: List<SpeakerTalkViewModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerTalkViewHolder {
        val binding = ItemSpeakerTalkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpeakerTalkViewHolder(binding)
    }


    override fun getItemCount() = speakerTalkViewModels.size

    override fun onBindViewHolder(holder: SpeakerTalkViewHolder, position: Int) {
        holder.bind(speakerTalkViewModels[position])
    }

    fun setData(newSpeakerTalkViewModels: List<SpeakerTalkViewModel>) {
        speakerTalkViewModels = newSpeakerTalkViewModels
    }
}

class SpeakerTalkViewHolder(val binding: ItemSpeakerTalkBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(speakerTalkViewModel: SpeakerTalkViewModel) {
        binding.speakerTalkViewModel = speakerTalkViewModel
        binding.executePendingBindings()
    }
}
