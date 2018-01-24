package pl.droidsonroids.toast.app.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.OnRebindCallback
import android.os.Bundle
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.data.dto.event.TalkDto
import pl.droidsonroids.toast.databinding.ActivityTalkDetailsBinding
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.event.TalkDetailsViewModel
import javax.inject.Inject

class TalkDetailsActivity : BaseActivity() {
    companion object {
        private const val TALK_DTO_KEY = "talkDto"
        private const val EVENT_ID_KEY = "event_key"
        fun createIntent(context: Context, navigationRequest: NavigationRequest.TalkDetails): Intent {
            return Intent(context, TalkDetailsActivity::class.java)
                    .putExtra(TALK_DTO_KEY, navigationRequest.talkDto)
                    .putExtra(EVENT_ID_KEY, navigationRequest.eventId)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val talkDto by lazy {
        intent.getParcelableExtra<TalkDto>(TALK_DTO_KEY)
    }

    private val eventId by lazy {
        intent.getLongExtra(EVENT_ID_KEY, Constants.NO_ID)
    }

    private val talkDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(talkDto.id.toString(), TalkDetailsViewModel::class.java)
    }

    private var navigationDisposable: Disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        val binding = ActivityTalkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel(binding)
        setupBinding(binding)
    }

    private fun setupBinding(binding: ActivityTalkDetailsBinding) {
        binding.addOnRebindCallback(object : OnRebindCallback<ActivityTalkDetailsBinding>() {
            override fun onBound(binding: ActivityTalkDetailsBinding?) {
                supportStartPostponedEnterTransition()
                binding?.removeOnRebindCallback(this)
            }
        })
        binding.executePendingBindings()
    }

    private fun setupViewModel(binding: ActivityTalkDetailsBinding) {
        talkDetailsViewModel.init(eventId, talkDto)
        navigationDisposable = talkDetailsViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
        binding.talkDetailsViewModel = talkDetailsViewModel
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