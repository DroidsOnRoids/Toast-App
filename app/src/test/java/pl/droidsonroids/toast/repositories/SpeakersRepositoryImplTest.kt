package pl.droidsonroids.toast.repositories

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.api.speaker.SpeakerDetailsResponse
import pl.droidsonroids.toast.data.api.speaker.SpeakersResponse
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepositoryImpl
import pl.droidsonroids.toast.services.SpeakerService
import pl.droidsonroids.toast.testSpeakerDetails
import pl.droidsonroids.toast.testSpeakerDetailsDto
import pl.droidsonroids.toast.testSpeakers
import pl.droidsonroids.toast.testSpeakersPage

class SpeakersRepositoryImplTest : RxTestBase() {
    private val speakerId = 0L

    @Mock
    lateinit var speakerService: SpeakerService

    @InjectMocks
    lateinit var speakerRepository: SpeakersRepositoryImpl


    @Test
    fun shouldReturnSpeakersPage() {
        val allPagesCount = 1
        val pageNumber = 1
        val speakersResponse = SpeakersResponse(testSpeakers, allPagesCount)
        whenever(speakerService.getSpeakers(pageNumber = pageNumber)).thenReturn(Single.just(speakersResponse))
        speakerRepository.getSpeakersPage(pageNumber)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue { it == testSpeakersPage }
    }

    @Test
    fun shouldReturnSpeakerDetails() {
        val speakersResponse = SpeakerDetailsResponse(testSpeakerDetails)
        whenever(speakerService.getSpeaker(speakerId)).thenReturn(Single.just(speakersResponse))
        speakerRepository.getSpeaker(speakerId)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue {
                    it.id == testSpeakerDetailsDto.id &&
                            it.bio == testSpeakerDetailsDto.bio &&
                            it.name == testSpeakerDetailsDto.name &&
                            it.job == testSpeakerDetailsDto.job
                }
    }

}

