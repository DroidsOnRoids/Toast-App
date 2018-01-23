package pl.droidsonroids.toast.viewmodels.speaker

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.testSpeakerDetailsDto
import pl.droidsonroids.toast.utils.LoadingStatus

class SpeakerDetailsViewModelTest : RxTestBase() {
    private val speakerId = 0L

    @Mock
    lateinit var speakersRepository: SpeakersRepository
    @InjectMocks
    lateinit var speakerDetailsViewModel: SpeakerDetailsViewModel

    @Test
    fun shouldLoadSpeakerDetails() {
        whenever(speakersRepository.getSpeaker(speakerId)).thenReturn(testSpeakerDetailsDto.toSingle())
        speakerDetailsViewModel.init(speakerId)

        assertSpeakerDetails()
        assertThat(speakerDetailsViewModel.loadingStatus.get(), CoreMatchers.equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldFailLoadSpeakerDetails() {
        whenever(speakersRepository.getSpeaker(speakerId)).thenReturn(Single.error(Exception()))
        speakerDetailsViewModel.init(speakerId)

        assertThat(speakerDetailsViewModel.loadingStatus.get(), CoreMatchers.equalTo(LoadingStatus.ERROR))
    }


    @Test
    fun shouldRetryLoadSpeakerDetails() {
        whenever(speakersRepository.getSpeaker(speakerId)).thenReturn(Single.error(Exception()))
        speakerDetailsViewModel.init(speakerId)

        whenever(speakersRepository.getSpeaker(speakerId)).thenReturn(testSpeakerDetailsDto.toSingle())

        speakerDetailsViewModel.retryLoading()

        assertSpeakerDetails()
        assertThat(speakerDetailsViewModel.loadingStatus.get(), CoreMatchers.equalTo(LoadingStatus.SUCCESS))
    }

    private fun assertSpeakerDetails() {
        assertThat(speakerDetailsViewModel.name.get(), CoreMatchers.equalTo(testSpeakerDetailsDto.name))
        assertThat(speakerDetailsViewModel.job.get(), CoreMatchers.equalTo(testSpeakerDetailsDto.job))
        assertThat(speakerDetailsViewModel.bio.get(), CoreMatchers.equalTo(testSpeakerDetailsDto.bio))
        assertThat(speakerDetailsViewModel.avatar.get(), CoreMatchers.equalTo(testSpeakerDetailsDto.avatar))
    }
}