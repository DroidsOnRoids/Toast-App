package pl.droidsonroids.toast.viewmodels.speaker

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.rule.RxPluginSchedulerRule
import pl.droidsonroids.toast.testSpeaker
import pl.droidsonroids.toast.testSpeakers
import pl.droidsonroids.toast.testSpeakersPage
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.SortingType
import pl.droidsonroids.toast.viewmodels.LoadingDelayViewModel
import java.util.concurrent.TimeUnit
import java.io.IOException

class SpeakersViewModelTest : RxTestBase() {
    private val testScheduler = TestScheduler()
    private val delayViewModel = LoadingDelayViewModel(clock = mock())

    @get:Rule
    override val rxPluginSchedulerRule = RxPluginSchedulerRule(testScheduler)

    @Mock
    lateinit var speakersRepository: SpeakersRepository
    @Mock
    lateinit var analyticsEventTracker: AnalyticsEventTracker

    lateinit var speakersViewModel: SpeakersViewModel

    @Test
    fun shouldLoadFirstPage() {
        val testSpeakersPageSingle = Single.just(testSpeakersPage)
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(testSpeakersPageSingle)
        setupSpeakersViewModel()

        checkIsFirstPageLoaded()
    }

    private fun checkIsFirstPageLoaded() {
        val speakerItemViewModelList = speakersViewModel.speakersSubject.value
        val testSpeaker = testSpeakers.first()
        val resultSpeakerViewModel = (speakerItemViewModelList.first() as? State.Item)?.item

        assertSpeaker(resultSpeakerViewModel, testSpeaker)
        assertThat(speakersViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    private fun assertSpeaker(resultSpeakerViewModel: SpeakerItemViewModel?, testSpeaker: ApiSpeaker) {
        assertThat(resultSpeakerViewModel?.id, equalTo(testSpeaker.id))
        assertThat(resultSpeakerViewModel?.name, equalTo(testSpeaker.name))
        assertThat(resultSpeakerViewModel?.avatar, equalTo(testSpeaker.avatar.toDto()))
    }


    @Test
    fun shouldFailLoadFirstPage() {
        val error = Single.error<Page<SpeakerDto>>(Exception())
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(error)
        setupSpeakersViewModel()

        val speakerItemViewModelList: List<State<SpeakerItemViewModel>> = speakersViewModel.speakersSubject.value

        assertThat(speakerItemViewModelList.isEmpty(), equalTo(true))
        assertThat(speakersViewModel.loadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }

    @Test
    fun shouldLoadFirstPageAfterRetry() {
        val error = Single.error<Page<SpeakerDto>>(Exception())
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(error)
        setupSpeakersViewModel()

        val testSpeakersPageSingle = Single.just(testSpeakersPage)
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(testSpeakersPageSingle)

        speakersViewModel.retryLoading()
        testScheduler.advanceTimeBy(Constants.MIN_LOADING_DELAY_MILLIS, TimeUnit.MILLISECONDS)

        checkIsFirstPageLoaded()
    }

    @Test
    fun shouldHaveLoadingItemWhenNextPageAvailable() {
        val testSpeakersPageWithNextPageAvailable = testSpeakersPage.copy(allPagesCount = 2)
        val firstSpeakerPageSingle = Single.just(testSpeakersPageWithNextPageAvailable)
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(firstSpeakerPageSingle)
        setupSpeakersViewModel()

        val speakerItemViewModelList = speakersViewModel.speakersSubject.value
        checkIsFirstPageLoaded()
        assertThat(speakerItemViewModelList.last(), equalTo(State.Loading as State<SpeakerItemViewModel>))
    }

    @Test
    fun shouldHaveErrorItemWhenNextPageLoadFailed() {
        val testSpeakersPageWithNextPageAvailable = testSpeakersPage.copy(allPagesCount = 2)
        val firstSpeakerPageSingle = Single.just(testSpeakersPageWithNextPageAvailable)
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(firstSpeakerPageSingle)
        val error = Single.error<Page<SpeakerDto>>(Exception())
        whenever(speakersRepository.getSpeakersPage(eq(2), any())).thenReturn(error)
        setupSpeakersViewModel()

        speakersViewModel.loadNextPage()

        val speakerItemViewModelList = speakersViewModel.speakersSubject.value
        assertThat(speakerItemViewModelList.last() is State.Error, equalTo(true))
    }

    @Test
    fun shouldLoadNextPage() {
        val itemsCountOnAllPages = 2
        val testSpeakersPageWithNextPageAvailable = testSpeakersPage.copy(allPagesCount = 2)
        val firstSpeakerPageSingle = Single.just(testSpeakersPageWithNextPageAvailable)
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(firstSpeakerPageSingle)

        val secondSpeakersPage = testSpeakersPage.copy(pageNumber = 2, allPagesCount = 2)
        val secondSpeakerPageSingle = Single.just(secondSpeakersPage)
        whenever(speakersRepository.getSpeakersPage(eq(2), any())).thenReturn(secondSpeakerPageSingle)

        setupSpeakersViewModel()

        speakersViewModel.loadNextPage()

        val speakerItemViewModelList = speakersViewModel.speakersSubject.value
        assertThat(speakerItemViewModelList.size, equalTo(itemsCountOnAllPages))
        assertSpeaker((speakerItemViewModelList[1] as? State.Item)?.item, testSpeakers.first())
    }

    @Test
    fun shouldRequestNavigationToSpeakerDetails() {
        val testSpeakerSingle = Single.just(testSpeakersPage)
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(testSpeakerSingle)
        setupSpeakersViewModel()

        val speakerItemViewModelList = speakersViewModel.speakersSubject.value
        val speakerItemViewModel = (speakerItemViewModelList.first() as? State.Item)?.item
        val testObserver = speakersViewModel.navigationSubject.test()

        speakerItemViewModel?.onClick()

        testObserver.assertValue {
            it is NavigationRequest.SpeakerDetails
                    && it.id == testSpeaker.id
        }
    }

    @Test
    fun shouldSortSpeakersAlphabetical() {
        val error = Single.error<Page<SpeakerDto>>(Exception())
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(error)
        setupSpeakersViewModel()

        val testSpeakersPageSingle = Single.just(testSpeakersPage)
        whenever(speakersRepository.getSpeakersPage(sortingQuery = SortingType.ALPHABETICAL.toQuery())).thenReturn(testSpeakersPageSingle)

        speakersViewModel.onAlphabeticalSortingClick()
        assertThat(speakersViewModel.sortingType.get(), equalTo(SortingType.ALPHABETICAL))
    }

    @Test
    fun shouldSortSpeakersByDate() {
        val error = Single.error<Page<SpeakerDto>>(Exception())
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(error)
        setupSpeakersViewModel()

        whenever(speakersRepository.getSpeakersPage(sortingQuery = SortingType.DATE.toQuery())).thenReturn(Single.just(testSpeakersPage))

        speakersViewModel.onDateSortingClick()
        assertThat(speakersViewModel.sortingType.get(), equalTo(SortingType.DATE))
    }

    @Test
    fun shouldLoadFirstPageAfterDateSorting() {
        val testSpeakerSingle = Single.just(testSpeakersPage)
        whenever(speakersRepository.getSpeakersPage(sortingQuery = SortingType.DATE.toQuery())).thenReturn(testSpeakerSingle)
        setupSpeakersViewModel()

        speakersViewModel.onDateSortingClick()

        checkIsFirstPageLoaded()
    }

    @Test
    fun shouldLoadFirstPageAfterAlphabeticalSorting() {
        val error = Single.error<Page<SpeakerDto>>(Exception())
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(error)
        setupSpeakersViewModel()

        val testSpeakersPageSingle = Single.just(testSpeakersPage)
        whenever(speakersRepository.getSpeakersPage(pageNumber = any(), sortingQuery = eq(SortingType.ALPHABETICAL.toQuery()))).thenReturn(testSpeakersPageSingle)

        speakersViewModel.onAlphabeticalSortingClick()
        testScheduler.advanceTimeBy(Constants.MIN_LOADING_DELAY_MILLIS, TimeUnit.MILLISECONDS)

        checkIsFirstPageLoaded()
    }

    @Test
    fun shouldRefreshData() {
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.just(Page(items = emptyList(), pageNumber = 1, allPagesCount = 1)))
        setupSpeakersViewModel()
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.just(testSpeakersPage))

        speakersViewModel.refresh()

        checkIsFirstPageLoaded()
    }

    @Test
    fun shouldHideSwipeRefreshLoaderWhenDataRefreshed() {
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.just(Page(items = emptyList(), pageNumber = 1, allPagesCount = 1)))
        setupSpeakersViewModel()
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.just(testSpeakersPage))
        val testObserver = speakersViewModel.isSwipeRefreshLoaderVisibleSubject.test()

        speakersViewModel.refresh()

        testObserver.assertValue { !it }
    }

    @Test
    fun shouldRetainDataWhenRefreshFailed() {
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.just(testSpeakersPage))
        setupSpeakersViewModel()
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.error(IOException()))

        speakersViewModel.refresh()

        checkIsFirstPageLoaded()
    }

    @Test
    fun shouldRequestSnackBarWhenRefreshFailed() {
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.just(testSpeakersPage))
        setupSpeakersViewModel()
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.error(IOException()))
        val testObserver = speakersViewModel.navigationSubject.test()

        speakersViewModel.refresh()

        testObserver.assertValue { it is NavigationRequest.SnackBar }
    }


    @Test
    fun shouldHideSwipeRefreshLoaderWhenRefreshFailed() {
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.just(Page(items = emptyList(), pageNumber = 1, allPagesCount = 1)))
        setupSpeakersViewModel()
        whenever(speakersRepository.getSpeakersPage(any(), any())).thenReturn(Single.error(IOException()))
        val testObserver = speakersViewModel.isSwipeRefreshLoaderVisibleSubject.test()

        speakersViewModel.refresh()

        testObserver.assertValue { !it }
    }

    private fun setupSpeakersViewModel() {
        speakersViewModel = SpeakersViewModel(speakersRepository, analyticsEventTracker, delayViewModel)
        testScheduler.advanceTimeBy(Constants.MIN_LOADING_DELAY_MILLIS, TimeUnit.MILLISECONDS)
    }
}