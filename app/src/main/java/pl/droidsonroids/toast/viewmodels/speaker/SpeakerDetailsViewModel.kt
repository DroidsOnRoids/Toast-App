package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDetailsDto
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import javax.inject.Inject


class SpeakerDetailsViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : ViewModel(), LoadingViewModel {
    private val Any.simpleClassName: String get() = javaClass.simpleName
    private var speakerId: Long? = null

    override val loadingStatus = ObservableField(LoadingStatus.PENDING)
    val name = ObservableField("")
    val job = ObservableField("")
    val bio = ObservableField("")
    val avatar = ObservableField<ImageDto?>()


    fun init(id: Long) {
        if (speakerId == null) {
            speakerId = id
            loadSpeaker()
        }
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
