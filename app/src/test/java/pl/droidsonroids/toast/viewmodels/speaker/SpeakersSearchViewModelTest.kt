package pl.droidsonroids.toast.viewmodels.speaker

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.internal.verification.VerificationModeFactory.times
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.testSpeakers
import pl.droidsonroids.toast.testSpeakersPage
import pl.droidsonroids.toast.utils.LoadingStatus

class SpeakersSearchViewModelTest : RxTestBase() {

    @Mock
    lateinit var speakersRepository: SpeakersRepository
    lateinit var speakersSearchViewModel: SpeakersSearchViewModel


    @Test
    fun shouldSearch() {
        val query = "test"
        whenever(speakersRepository.searchSpeakersPage(query)).thenReturn(Single.just(testSpeakersPage))
        speakersSearchViewModel = SpeakersSearchViewModel(speakersRepository)

        speakersSearchViewModel.searchPhrase.set(query)

        checkIsFirstPageLoaded()
        assertThat(speakersSearchViewModel.noItemsFound.get(), `is`(false))
    }

    @Test
    fun shouldSearchSamePhraseOnlyOnce() {
        val query = "test"
        whenever(speakersRepository.searchSpeakersPage(query)).thenReturn(Single.just(testSpeakersPage))
        speakersSearchViewModel = SpeakersSearchViewModel(speakersRepository)

        speakersSearchViewModel.searchPhrase.set(query)

        speakersSearchViewModel.searchPhrase.set(query)

        speakersSearchViewModel.requestSearch()

        verify(speakersRepository, times(1)).searchSpeakersPage(query)
    }

    @Test
    fun shouldClearListAfterNewSearchWithEmptyList() {
        val firstQuery = "test"
        val secondQuery = "test2"
        whenever(speakersRepository.searchSpeakersPage(firstQuery)).thenReturn(Single.just(testSpeakersPage))
        whenever(speakersRepository.searchSpeakersPage(secondQuery)).thenReturn(Single.just(Page(emptyList(), 1, 1)))
        speakersSearchViewModel = SpeakersSearchViewModel(speakersRepository)

        speakersSearchViewModel.searchPhrase.set(firstQuery)

        speakersSearchViewModel.searchPhrase.set(secondQuery)

        val speakerItemViewModelList = speakersSearchViewModel.speakersSubject.value
        assertThat(speakerItemViewModelList.isEmpty(), `is`(true))
        assertThat(speakersSearchViewModel.noItemsFound.get(), `is`(true))
    }

    @Test
    fun shouldHaveItemsAfterNewSearch() {
        val firstQuery = "test"
        val secondQuery = "test2"
        whenever(speakersRepository.searchSpeakersPage(firstQuery)).thenReturn(Single.just(Page(emptyList(), 1, 1)))
        whenever(speakersRepository.searchSpeakersPage(secondQuery)).thenReturn(Single.just(testSpeakersPage))
        speakersSearchViewModel = SpeakersSearchViewModel(speakersRepository)

        speakersSearchViewModel.searchPhrase.set(firstQuery)

        speakersSearchViewModel.searchPhrase.set(secondQuery)

        checkIsFirstPageLoaded()
        assertThat(speakersSearchViewModel.noItemsFound.get(), `is`(false))
    }

    private fun checkIsFirstPageLoaded() {
        val speakerItemViewModelList = speakersSearchViewModel.speakersSubject.value
        val testSpeaker = testSpeakers.first()
        val resultSpeakerViewModel = (speakerItemViewModelList.first() as? State.Item)?.item

        assertSpeaker(resultSpeakerViewModel, testSpeaker)
        assertThat(speakersSearchViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    private fun assertSpeaker(resultSpeakerViewModel: SpeakerItemViewModel?, testSpeaker: ApiSpeaker) {
        assertThat(resultSpeakerViewModel?.id, equalTo(testSpeaker.id))
        assertThat(resultSpeakerViewModel?.name, equalTo(testSpeaker.name))
        assertThat(resultSpeakerViewModel?.avatar, equalTo(testSpeaker.avatar.toDto()))
    }
}