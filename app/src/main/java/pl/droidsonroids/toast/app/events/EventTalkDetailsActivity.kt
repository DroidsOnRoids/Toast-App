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
import pl.droidsonroids.toast.data.dto.event.EventTalkDto
import pl.droidsonroids.toast.databinding.ActivityEventTalkDetailsBinding
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.event.EventTalkDetailsViewModel
import javax.inject.Inject

class EventTalkDetailsActivity : BaseActivity() {
    companion object {
        private const val TALK_DTO_KEY = "speakerTalkDto"
        fun createIntent(context: Context, navigationRequest: NavigationRequest.EventTalkDetails): Intent {
            return Intent(context, EventTalkDetailsActivity::class.java)
                    .putExtra(TALK_DTO_KEY, navigationRequest.eventTalkDto)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val talkDto by lazy {
        intent.getParcelableExtra<EventTalkDto>(TALK_DTO_KEY)
    }

    private val eventTalkDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(talkDto.id.toString(), EventTalkDetailsViewModel::class.java)
    }

    private var navigationDisposable: Disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        val binding = ActivityEventTalkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel(binding)
        setupBinding(binding)
    }

    private fun setupBinding(binding: ActivityEventTalkDetailsBinding) {
        binding.addOnRebindCallback(object : OnRebindCallback<ActivityEventTalkDetailsBinding>() {
            override fun onBound(binding: ActivityEventTalkDetailsBinding?) {
                supportStartPostponedEnterTransition()
                binding?.removeOnRebindCallback(this)
            }
        })
        binding.executePendingBindings()
    }

    private fun setupViewModel(binding: ActivityEventTalkDetailsBinding) {
        eventTalkDetailsViewModel.init(talkDto)
        navigationDisposable = eventTalkDetailsViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
        binding.eventTalkDetailsViewModel = eventTalkDetailsViewModel
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