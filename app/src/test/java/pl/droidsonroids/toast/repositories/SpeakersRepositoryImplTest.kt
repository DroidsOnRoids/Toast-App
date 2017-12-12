package pl.droidsonroids.toast.repositories

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.api.speaker.SpeakersResponse
import pl.droidsonroids.toast.services.SpeakerService
import pl.droidsonroids.toast.testSpeakers
import pl.droidsonroids.toast.testSpeakersPage

class SpeakersRepositoryImplTest : RxTestBase() {
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

}

