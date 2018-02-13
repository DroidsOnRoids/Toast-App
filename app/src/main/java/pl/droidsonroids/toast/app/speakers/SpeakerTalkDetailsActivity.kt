package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.databinding.ActivitySpeakerTalkDetailsBinding
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.speaker.SpeakerTalkDetailsViewModel
import javax.inject.Inject


class SpeakerTalkDetailsActivity : BaseActivity() {
    companion object {
        private const val TALK_DTO_KEY = "eventTalkDto"
        fun createIntent(context: Context, navigationRequest: NavigationRequest.SpeakerTalkDetails): Intent {
            return Intent(context, SpeakerTalkDetailsActivity::class.java)
                    .putExtra(TALK_DTO_KEY, navigationRequest.speakerTalkDto)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val talkDto by lazy {
        intent.getParcelableExtra<SpeakerTalkDto>(TALK_DTO_KEY)
    }

    private val speakerTalkDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(talkDto.id.toString(), SpeakerTalkDetailsViewModel::class.java)
    }

    private var navigationDisposable: Disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        val binding = ActivitySpeakerTalkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel(binding)
    }

    private fun setupViewModel(binding: ActivitySpeakerTalkDetailsBinding) {
        speakerTalkDetailsViewModel.init(talkDto) {
            supportStartPostponedEnterTransition()
        }
        navigationDisposable = speakerTalkDetailsViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
        binding.speakerTalkDetailsViewModel = speakerTalkDetailsViewModel
    }

    private fun handleNavigationRequest(it: NavigationRequest) {
        if (it is NavigationRequest.Close) {
            finishAfterTransition()
        } else {
            navigator.dispatch(this, it)
        }
    }

    override fun onDestroy() {
        navigationDisposable.dispose()
        super.onDestroy()
    }
}