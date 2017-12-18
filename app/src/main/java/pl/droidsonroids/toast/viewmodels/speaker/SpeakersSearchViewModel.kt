package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.data.wrapWithState
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.toObservable
import pl.droidsonroids.toast.utils.toPage
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeakersSearchViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : ViewModel(), LoadingViewModel {
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.SUCCESS)
    val speakersSubject: BehaviorSubject<List<State<SpeakerItemViewModel>>> = BehaviorSubject.create()
    val searchPhrase: ObservableField<String> = ObservableField("")
    private val searchObservable: Observable<String> = searchPhrase.toObservable()
    val noItemsFound: ObservableField<Boolean> = ObservableField(false)

    private var isNextPageLoading: Boolean = false
    private var nextPageNumber: Int? = null
    private var searchDisposable: Disposable? = null
    private var firstPageDisposable: Disposable? = null
    private var nextPageDisposable: Disposable? = null
    private val Any.simpleClassName: String get() = javaClass.simpleName

    init {
        searchDisposable = searchObservable
                .debounce(1, TimeUnit.SECONDS)
                .filter { it.isNotEmpty() }
                .doOnNext { disposePreviousLoad() }
                .switchMapSingle(::searchSpeakers)
                .doAfterNext(::isEmptyResponse)
                .subscribeBy(
                        onNext = (::onNewSpeakersPageLoaded),
                        onError = (::onFirstPageLoadError)
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
        disposePreviousLoad()
        firstPageDisposable = searchSpeakers(searchPhrase.get())
                .subscribeBy(
                        onSuccess = (::onSpeakersPageLoaded),
                        onError = (::onNextPageLoadError)
                )
    }

    private fun mapToSingleSpeakerItemViewModelsPage(page: Page<SpeakerDto>): Single<Page<State.Item<SpeakerItemViewModel>>> {
        val (items, pageNumber, allPagesCount) = page
        return items.toObservable()
                .map {
                    it.toViewModel { id ->
                        Log.d(simpleClassName, "Event item clicked $id")
                    }
                }
                .map { wrapWithState(it) }
                .toPage(pageNumber, allPagesCount)
    }

    private fun onSpeakersPageLoaded(page: Page<State.Item<SpeakerItemViewModel>>) {
        val speakers = getSpeakers(page)
        speakersSubject.onNext(speakers)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun getSpeakers(page: Page<State.Item<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
        val speakers = mergeWithExistingSpeakers(page.items)
        return speakers.addLoadingIfNextPageAvailable(page)
    }

    private fun List<State<SpeakerItemViewModel>>.addLoadingIfNextPageAvailable(page: Page<State.Item<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
        return if (page.pageNumber < page.allPagesCount) {
            nextPageNumber = page.pageNumber + 1
            this + State.Loading
        } else {
            nextPageNumber = null
            this
        }
    }

    private fun mergeWithExistingSpeakers(newList: List<State<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
        val previousList = speakersSubject.value
                ?.filter { it is State.Item }
                ?: listOf()
        return previousList + newList
    }

    private fun onFirstPageLoadError(throwable: Throwable) {
        loadingStatus.set(LoadingStatus.ERROR)
        Log.e(simpleClassName, "Something went wrong with fetching data for SpeakersSearchViewModel", throwable)
    }

    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isNextPageLoading && nextPageNumber != null) {
            loadPage(searchPhrase.get(), nextPageNumber)
        }
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

    private fun onNextPageLoadError(throwable: Throwable) {
        val speakers = mergeWithExistingSpeakers(listOf(createErrorState()))
        speakersSubject.onNext(speakers)
        Log.e(simpleClassName, "Something went wrong with fetching next speakers page for SpeakersSearchViewModel", throwable)
    }

    private fun createErrorState(): State.Error {
        return State.Error(::onErrorClick)
    }

    private fun onErrorClick() {
        val previousEvents = mergeWithExistingSpeakers(listOf(State.Loading))
        speakersSubject.onNext(previousEvents)
        nextPageNumber?.let { loadPage(searchPhrase.get(), it) }
    }

    override fun onCleared() {
        searchDisposable?.dispose()
        disposePreviousLoad()
    }
}