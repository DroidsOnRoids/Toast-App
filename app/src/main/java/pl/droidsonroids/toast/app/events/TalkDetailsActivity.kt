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
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.event.TalkDetailsViewModel
import javax.inject.Inject

class TalkDetailsActivity : BaseActivity() {
    companion object {
        private const val TALK_DTO_KEY = "talkDto"
        fun createIntent(context: Context, navigationRequest: NavigationRequest.TalkDetails): Intent {
            return Intent(context, TalkDetailsActivity::class.java)
                    .putExtra(TALK_DTO_KEY, navigationRequest.talkDto)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val talkDto by lazy {
        intent.getParcelableExtra<TalkDto>(TALK_DTO_KEY)
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
        binding.addOnRebindCallback(object : OnRebindCallback<ActivityTalkDetailsBinding>() {
            override fun onBound(binding: ActivityTalkDetailsBinding?) {
                supportStartPostponedEnterTransition()
            }
        })
        binding.executePendingBindings()
    }

    private fun setupViewModel(binding: ActivityTalkDetailsBinding) {
        talkDetailsViewModel.init(talkDto)
        navigationDisposable = talkDetailsViewModel.navigationSubject
                .subscribe {
                    if (it is NavigationRequest.Close) {
                        finishAfterTransition()
                    } else {
                        navigator.dispatch(this, it)
                    }
                }
        binding.talkDetailsViewModel = talkDetailsViewModel
    }

    override fun onDestroy() {
        navigationDisposable.dispose()
        super.onDestroy()
    }
}