package pl.droidsonroids.toast.viewmodels.speaker

import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.toObservable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeakersSearchViewModel @Inject constructor(
        private val speakersRepository: SpeakersRepository,
        private val analyticsEventTracker: AnalyticsEventTracker
) : BaseSpeakerListViewModel() {
    val searchPhrase: ObservableField<String> = ObservableField("")
    private val searchObservable: Observable<String> = searchPhrase.toObservable()
    private var lastSearchedPhrase: String = ""
    val noItemsFound: ObservableField<Boolean> = ObservableField(false)

    private var searchDisposable: Disposable? = null
    private var firstPageDisposable: Disposable? = null
    private var nextPageDisposable: Disposable? = null

    init {
        subscribeToSearchTextChanges()
    }

    private fun subscribeToSearchTextChanges() {
        searchDisposable = searchObservable
                .debounce(1, TimeUnit.SECONDS)
                .map(String::trim)
                .filter(::shouldPerformSearch)
                .doOnNext { disposePreviousLoad() }
                .doOnNext { lastSearchedPhrase = it }
                .switchMapSingle(::searchSpeakers)
                .doOnError(::onSearchingError)
                .retry()
                .subscribeBy(
                        onNext = (::onSearchingSuccess)
                )
    }

    private fun onSearchingSuccess(it: Page<State.Item<SpeakerItemViewModel>>) {
        onNewSpeakersPageLoaded(it)
        analyticsEventTracker.logSearchPhraseEvent(lastSearchedPhrase)
    }

    private fun onSearchingError(it: Throwable) {
        onFirstPageLoadError(it)
        analyticsEventTracker.logSearchPhraseEvent(lastSearchedPhrase)
    }

    private fun shouldPerformSearch(query: String) =
            query.isNotEmpty() && (query != lastSearchedPhrase || loadingStatus.get() == LoadingStatus.ERROR)

    private fun disposePreviousLoad() {
        firstPageDisposable?.dispose()
        nextPageDisposable?.dispose()
    }

    private fun searchSpeakers(query: String): Single<Page<State.Item<SpeakerItemViewModel>>> {
        loadingStatus.set(LoadingStatus.PENDING)
        return speakersRepository.searchSpeakersPage(query)
                .flatMap(::mapToSingleSpeakerItemViewModelsPage)
                .doAfterSuccess(::isEmptyResponse)
    }

    private fun isEmptyResponse(page: Page<State<SpeakerItemViewModel>>) {
        noItemsFound.set(page.items.isEmpty())
    }

    override fun retryLoading() {
        search(lastSearchedPhrase)
    }

    fun requestSearch() {
        val query = searchPhrase.get().trim()
        if (shouldPerformSearch(query)) {
            search(query)
        }
    }

    private fun search(query: String) {
        disposePreviousLoad()
        lastSearchedPhrase = query
        firstPageDisposable = searchSpeakers(query)
                .subscribeBy(
                        onSuccess = {
                            onNewSpeakersPageLoaded(it)
                            analyticsEventTracker.logSearchPhraseEvent(query)
                        },
                        onError = {
                            onFirstPageLoadError(it)
                            analyticsEventTracker.logSearchPhraseEvent(query)
                        }
                )
    }


    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isNextPageLoading && nextPageNumber != null) {
            loadPage(lastSearchedPhrase, nextPageNumber)
        }
    }

    override fun onErrorClick() {
        val previousEvents = mergeWithExistingSpeakers(listOf(State.Loading))
        speakersSubject.onNext(previousEvents)
        nextPageNumber?.let { loadPage(lastSearchedPhrase, it) }
    }


    private fun loadPage(query: String, pageNumber: Int) {
        isNextPageLoading = true
        nextPageDisposable = speakersRepository.searchSpeakersPage(query, pageNumber)
                .flatMap(::mapToSingleSpeakerItemViewModelsPage)
                .doAfterSuccess { isNextPageLoading = false }
                .subscribeBy(
                        onSuccess = (::onSpeakersPageLoaded),
                        onError = (::onNextPageLoadError)
                )
    }

    override fun onSpeakerNavigationRequestSent(speakerName: String) {
        analyticsEventTracker.logSearchShowSpeakerEvent(speakerName)
    }

    override fun onCleared() {
        searchDisposable?.dispose()
        firstPageDisposable?.dispose()
        nextPageDisposable?.dispose()
        disposePreviousLoad()
    }
}