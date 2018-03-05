package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.layout_event_item.*
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

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        val binding = ActivitySpeakerTalkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel(binding)
    }

    private fun setupViewModel(binding: ActivitySpeakerTalkDetailsBinding) {
        speakerTalkDetailsViewModel.init(talkDto)
        compositeDisposable += speakerTalkDetailsViewModel.navigationSubject
                .subscribe(::handleNavigationRequest)
        compositeDisposable += speakerTalkDetailsViewModel.coverLoadingFinishedSubject
                .take(1)
                .subscribe { supportStartPostponedEnterTransition() }
        binding.speakerTalkDetailsViewModel = speakerTalkDetailsViewModel
    }

    private fun handleNavigationRequest(request: NavigationRequest) {
        when (request) {
            NavigationRequest.Close -> finishAfterTransition()
            is NavigationRequest.EventDetails -> navigator.showActivityWithSharedAnimation(this, request, getSharedViews())
            else -> navigator.dispatch(this, request)
        }
    }

    private fun getSharedViews() = arrayOf(Pair(eventCoverImage as View, eventCoverImage.transitionName))

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}