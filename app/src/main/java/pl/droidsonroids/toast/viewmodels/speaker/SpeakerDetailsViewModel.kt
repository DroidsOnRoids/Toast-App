package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import javax.inject.Inject


class SpeakerDetailsViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : ViewModel() {
    private val Any.simpleClassName: String get() = javaClass.simpleName
    private var speakerId: Long? = null

    val name = ObservableField("")
    val job = ObservableField("")
    val avatar = ObservableField<ImageDto?>()


    fun init(id: Long) {
        if (speakerId == null) {
            speakerId = id
            loadSpeaker()
        }
    }

    private fun loadSpeaker() {
        speakerId?.let {
            speakersRepository.getSpeaker(it)
                    .subscribeBy(
                            onSuccess = (::onSpeakerLoaded),
                            onError = (::onSpeakerLoadError)
                    )
        }
    }

    private fun onSpeakerLoaded(speakerDto: SpeakerDto) {
        speakerDto.let {
            name.set(it.name)
            job.set(it.job)
            avatar.set(it.avatar)
        }
    }

    private fun onSpeakerLoadError(throwable: Throwable) {
        Log.e(simpleClassName, "Something went wrong when fetching event details with id = $speakerId", throwable)
    }
}
