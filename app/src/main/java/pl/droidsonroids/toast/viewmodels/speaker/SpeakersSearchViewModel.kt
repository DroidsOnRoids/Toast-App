package pl.droidsonroids.toast.viewmodels.speaker

import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.toObservable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeakersSearchViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : BaseSpeakerListViewModel() {
    val searchPhrase: ObservableField<String> = ObservableField("")
    private val searchObservable: Observable<String> = searchPhrase.toObservable()
    private var lastSearchPhrase: String = ""
    val noItemsFound: ObservableField<Boolean> = ObservableField(false)

    private var searchDisposable: Disposable? = null
    private var firstPageDisposable: Disposable? = null
    private var nextPageDisposable: Disposable? = null

    init {
        searchDisposable = searchObservable
                .debounce(1, TimeUnit.SECONDS)
                .filter { it.isNotEmpty() && it != lastSearchPhrase }
                .doOnNext { disposePreviousLoad() }
                .doOnNext { lastSearchPhrase = it }
                .switchMapSingle(::searchSpeakers)
                .doAfterNext(::isEmptyResponse)
                .doOnError(::onFirstPageLoadError)
                .retry()
                .subscribeBy(
                        onNext = (::onNewSpeakersPageLoaded)
                )
    }

    private fun onNewSpeakersPageLoaded(page: Page<State.Item<SpeakerItemViewModel>>) {
        val speakers = page.items.addLoadingIfNextPageAvailable(page)
        speakersSubject.onNext(speakers)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun disposePreviousLoad() {
        firstPageDisposable?.dispose()
        nextPageDisposable?.dispose()
    }

    private fun searchSpeakers(query: String): Single<Page<State.Item<SpeakerItemViewModel>>> {
        loadingStatus.set(LoadingStatus.PENDING)
        return speakersRepository.searchSpeakersPage(query)
                .flatMap(::mapToSingleSpeakerItemViewModelsPage)
    }

    private fun isEmptyResponse(page: Page<State<SpeakerItemViewModel>>) {
        noItemsFound.set(page.items.isEmpty())
    }

    override fun retryLoading() {
        search(lastSearchPhrase)
    }

    fun search() {
        val query = searchPhrase.get()
        if (query != lastSearchPhrase) {
            search(query)
        }
    }

    private fun search(query: String) {
        disposePreviousLoad()
        firstPageDisposable = searchSpeakers(query)
                .subscribeBy(
                        onSuccess = (::onNewSpeakersPageLoaded),
                        onError = (::onFirstPageLoadError)
                )
    }


    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isNextPageLoading && nextPageNumber != null) {
            loadPage(searchPhrase.get(), nextPageNumber)
        }
    }

    override fun onErrorClick() {
        val previousEvents = mergeWithExistingSpeakers(listOf(State.Loading))
        speakersSubject.onNext(previousEvents)
        nextPageNumber?.let { loadPage(lastSearchPhrase, it) }
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

    override fun onCleared() {
        searchDisposable?.dispose()
        disposePreviousLoad()
    }
}