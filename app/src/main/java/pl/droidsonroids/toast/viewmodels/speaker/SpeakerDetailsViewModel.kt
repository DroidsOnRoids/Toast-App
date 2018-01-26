package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDetailsDto
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject


class SpeakerDetailsViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : ViewModel(), LoadingViewModel, NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    private val Any.simpleClassName: String get() = javaClass.simpleName
    private var speakerId: Long? = null

    override val loadingStatus = ObservableField(LoadingStatus.PENDING)
    val name = ObservableField("")
    val job = ObservableField("")
    val bio = ObservableField("")
    val avatar = ObservableField<ImageDto?>()
    val github = ObservableField<String?>(null)
    val website = ObservableField<String?>(null)
    val twitter = ObservableField<String?>(null)
    val email = ObservableField<String?>(null)


    fun init(id: Long) {
        if (speakerId == null) {
            speakerId = id
            loadSpeaker()
        }
    }

    fun onGithubClick() {
        openWebsite(github.get())
    }

    fun onWebsiteClick() {
        openWebsite(website.get())
    }

    fun onTwitterClick() {
        openWebsite(twitter.get())
    }

    fun onEmailClick() {
        email.get()?.let { navigationSubject.onNext(NavigationRequest.Email(email = it)) }
    }

    private fun openWebsite(url: String?) {
        url?.let { navigationSubject.onNext(NavigationRequest.Website(url = it)) }
    }

    private fun loadSpeaker() {
        speakerId?.let {
            loadingStatus.set(LoadingStatus.PENDING)
            speakersRepository.getSpeaker(it)
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
        }

        loadMockLinks()
    }

    private fun loadMockLinks() {
        if (name.get() == "Test Testowski") {
            github.set("https://github.com/panwrona")
            website.set("https://www.thedroidsonroids.com/")
            twitter.set("https://twitter.com/droidsonroids")
            email.set("mariusz.brona@gmail.com")
        }
    }

    private fun onSpeakerLoadError(throwable: Throwable) {
        loadingStatus.set(LoadingStatus.ERROR)
        Log.e(simpleClassName, "Something went wrong when fetching event details with id = $speakerId", throwable)
    }

    override fun retryLoading() {
        loadSpeaker()
    }
}
