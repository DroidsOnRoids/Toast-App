package pl.droidsonroids.toast.viewmodels.speaker

import android.databinding.ObservableField
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.SortingType
import pl.droidsonroids.toast.utils.addOnPropertyChangedCallback
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val MIN_LOADING_DELAY = 500
class SpeakersViewModel @Inject constructor(
        private val speakersRepository: SpeakersRepository,
        private val analyticsEventTracker: AnalyticsEventTracker,
        private val clock: Clock
) : BaseSpeakerListViewModel() {

    val isSortingDetailsVisible: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val sortingType = ObservableField(SortingType.DATE)
    private var lastLoadingStartTimeMillis = clock.elapsedRealtime()

    private var speakersDisposable: Disposable? = null

    init {
        loadFirstPage()
        sortingType.addOnPropertyChangedCallback {
            loadFirstPage()
        }
    }

    override val isFadingEnabled get() = true

    private fun clearSpeakersList() {
        speakersSubject.onNext(emptyList())
    }

    fun toggleSortingDetailsVisibility() {
        isSortingDetailsVisible.onNext(!isSortingDetailsVisible.value)
    }

    fun onAlphabeticalSortingClick() {
        sortingType.set(SortingType.ALPHABETICAL)
        toggleSortingDetailsVisibility()
        analyticsEventTracker.logSpeakersChooseSortOptionEvent(sortingType.get())
    }

    fun onDateSortingClick() {
        sortingType.set(SortingType.DATE)
        toggleSortingDetailsVisibility()
        analyticsEventTracker.logSpeakersChooseSortOptionEvent(sortingType.get())
    }

    override fun retryLoading() {
        loadFirstPage()
    }

    private fun loadFirstPage() {
        isNextPageLoading = true
        loadingStatus.set(LoadingStatus.PENDING)
        lastLoadingStartTimeMillis = clock.elapsedRealtime()
        speakersDisposable = speakersRepository.getSpeakersPage(sortingQuery = sortingType.get().toQuery())
                .flatMap(::mapToSingleSpeakerItemViewModelsPage)
                .doOnSuccess { clearSpeakersList() }
                .addLoadingDelay()
                .doAfterSuccess { isNextPageLoading = false }
                .subscribeBy(
                        onSuccess = (::onNewSpeakersPageLoaded),
                        onError = (::onFirstPageLoadError)
                )
    }

    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isNextPageLoading && nextPageNumber != null) {
            loadPage(nextPageNumber)
        }
    }

    private fun loadPage(pageNumber: Int) {
        isNextPageLoading = true
        speakersDisposable = speakersRepository.getSpeakersPage(pageNumber, sortingType.get().toQuery())
                .flatMap(::mapToSingleSpeakerItemViewModelsPage)
                .doAfterSuccess { isNextPageLoading = false }
                .subscribeBy(
                        onSuccess = (::onSpeakersPageLoaded),
                        onError = (::onNextPageLoadError)
                )
    }

    override fun onErrorClick() {
        val previousEvents = mergeWithExistingSpeakers(listOf(State.Loading))
        speakersSubject.onNext(previousEvents)
        nextPageNumber?.let { loadPage(it) }
    }

    override fun onSpeakerNavigationRequestSent(speakerName: String) {
        analyticsEventTracker.logSpeakersShowSpeakerEvent(speakerName)
    }

    override fun onCleared() {
        speakersDisposable?.dispose()
    }

    private fun <T> Single<T>.addLoadingDelay() = flatMap {
        Single.just(it)
                .delay(MIN_LOADING_DELAY + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
    }

}
