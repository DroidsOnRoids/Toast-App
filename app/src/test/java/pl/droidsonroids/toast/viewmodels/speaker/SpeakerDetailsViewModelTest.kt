package pl.droidsonroids.toast.viewmodels.speaker

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import org.hamcrest.CoreMatchers.equalTo
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
        assertThat(speakerDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldFailLoadSpeakerDetails() {
        whenever(speakersRepository.getSpeaker(speakerId)).thenReturn(Single.error(Exception()))
        speakerDetailsViewModel.init(speakerId)

        assertThat(speakerDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }


    @Test
    fun shouldRetryLoadSpeakerDetails() {
        whenever(speakersRepository.getSpeaker(speakerId)).thenReturn(Single.error(Exception()))
        speakerDetailsViewModel.init(speakerId)

        whenever(speakersRepository.getSpeaker(speakerId)).thenReturn(testSpeakerDetailsDto.toSingle())

        speakerDetailsViewModel.retryLoading()

        assertSpeakerDetails()
        assertThat(speakerDetailsViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    private fun assertSpeakerDetails() {

        assertThat(speakerDetailsViewModel.name.get(), equalTo(testSpeakerDetailsDto.name))
        assertThat(speakerDetailsViewModel.job.get(), equalTo(testSpeakerDetailsDto.job))
        assertThat(speakerDetailsViewModel.bio.get(), equalTo(testSpeakerDetailsDto.bio))
        assertThat(speakerDetailsViewModel.avatar.get(), equalTo(testSpeakerDetailsDto.avatar))
    }
}