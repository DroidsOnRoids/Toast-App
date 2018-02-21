package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDetailsDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.DelayViewModel
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import timber.log.Timber
import javax.inject.Inject


class SpeakerDetailsViewModel @Inject constructor(
        private val speakersRepository: SpeakersRepository,
        private val analyticsEventTracker: AnalyticsEventTracker,
        delayViewModel: DelayViewModel
) : ViewModel(), LoadingViewModel, DelayViewModel by delayViewModel, NavigatingViewModel {
    private var speakerId: Long? = null

    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val loadingStatus = ObservableField(LoadingStatus.PENDING)
    override val isFadingEnabled get() = true

    val name = ObservableField("")
    val job = ObservableField("")
    val bio = ObservableField("")
    val avatar = ObservableField<ImageDto?>()
    val github = ObservableField<String?>(null)
    val website = ObservableField<String?>(null)
    val twitter = ObservableField<String?>(null)
    val email = ObservableField<String?>(null)

    val talksSubject: BehaviorSubject<List<SpeakerTalkViewModel>> = BehaviorSubject.create()

    fun init(id: Long) {
        if (speakerId == null) {
            speakerId = id
            loadSpeaker()
        }
    }

    fun onGithubClick() {
        github.get()?.let {
            openWebsite(github.get())
            analyticsEventTracker.logEventDetailsTapGithubEvent(it)
        }
    }

    fun onWebsiteClick() {
        website.get()?.let {
            openWebsite(website.get())
            analyticsEventTracker.logEventDetailsTapWebsiteEvent(it)
        }
    }

    fun onTwitterClick() {
        twitter.get()?.let {
            openWebsite(twitter.get())
            analyticsEventTracker.logEventDetailsTapTwitterEvent(it)
        }
    }

    fun onEmailClick() {
        email.get()?.let {
            navigationSubject.onNext(NavigationRequest.Email(email = it))
            analyticsEventTracker.logEventDetailsTapEmailEvent(it)
        }
    }

    private fun openWebsite(url: String?) {
        url?.let { navigationSubject.onNext(NavigationRequest.Website(url = it)) }
    }

    private fun loadSpeaker() {
        speakerId?.let {
            loadingStatus.set(LoadingStatus.PENDING)
            updateLastLoadingStartTime()
            speakersRepository.getSpeaker(it)
                    .let(::addLoadingDelay)
                    .subscribeBy(
                            onSuccess = (::onSpeakerLoaded),
                            onError = (::onSpeakerLoadError)
                    )
        }
    }

    private fun onSpeakerLoaded(speakerDto: SpeakerDetailsDto) {
        loadingStatus.set(LoadingStatus.SUCCESS)
        speakerDto.let {
            name.set(it.name)
            job.set(it.job)
            avatar.set(it.avatar)
            bio.set(it.bio)
            github.set(it.github)
            website.set(it.website)
            twitter.set(it.twitter)
            email.set(it.email)
            talksSubject.onNext(it.talks.map {
                it.toViewModel(::onReadMoreClick, ::onEventClick)
            })
        }
    }

    private fun onReadMoreClick(talkDto: SpeakerTalkDto) {
        navigationSubject.onNext(NavigationRequest.SpeakerTalkDetails(talkDto))
        analyticsEventTracker.logSpeakerDetailsReadMoreEvent(talkDto.title)
    }

    private fun onEventClick(eventId: Long) {
        navigationSubject.onNext(NavigationRequest.EventDetails(eventId))
        analyticsEventTracker.logSpeakerDetailsEventTapEvent(eventId)
    }

    private fun onSpeakerLoadError(throwable: Throwable) {
        loadingStatus.set(LoadingStatus.ERROR)
        Timber.e(throwable, "Something went wrong when fetching event details with id = $speakerId")
    }

    override fun retryLoading() {
        loadSpeaker()
    }
}
