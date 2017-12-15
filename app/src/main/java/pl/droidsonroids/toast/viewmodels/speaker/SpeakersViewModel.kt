package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
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
import pl.droidsonroids.toast.utils.toPage
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import javax.inject.Inject

class SpeakersViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : ViewModel(), LoadingViewModel {
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField()
    val speakersSubject: BehaviorSubject<List<State<SpeakerItemViewModel>>> = BehaviorSubject.create()
    val isSortingDetailsVisible: ObservableField<Boolean> = ObservableField()

    private var isNextPageLoading: Boolean = false
    private var nextPageNumber: Int? = null
    private var speakersDisposable: Disposable? = null
    private val Any.simpleClassName: String get() = javaClass.simpleName

    init {
        isSortingDetailsVisible.set(false)
        loadFirstPage()
    }

    fun toggleSortingDetailsVisibility() {
        isSortingDetailsVisible.set(!isSortingDetailsVisible.get())
    }

    override fun retryLoading() {
        loadFirstPage()
    }

    private fun loadFirstPage() {
        loadingStatus.set(LoadingStatus.PENDING)
        speakersDisposable = speakersRepository.getSpeakersPage()
                .flatMap(::mapToSingleSpeakerItemViewModelsPage)
                .subscribeBy(
                        onSuccess = (::onSpeakersPageLoaded),
                        onError = (::onFirstPageLoadError)
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
        var speakers = mergeWithExistingSpeakers(page.items)
        if (page.pageNumber < page.allPagesCount) {
            speakers += State.Loading
            nextPageNumber = page.pageNumber + 1
        } else {
            nextPageNumber = null
        }
        return speakers
    }

    private fun mergeWithExistingSpeakers(newList: List<State<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
        val previousList = speakersSubject.value
                ?.filter { it is State.Item }
                ?: listOf()
        return previousList + newList
    }

    private fun onFirstPageLoadError(throwable: Throwable) {
        loadingStatus.set(LoadingStatus.ERROR)
        Log.e(simpleClassName, "Something went wrong with fetching data for SpeakersViewModel", throwable)
    }

    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isNextPageLoading && nextPageNumber != null) {
            loadNextPage(nextPageNumber)
        }
    }

    private fun loadNextPage(pageNumber: Int) {
        isNextPageLoading = true
        speakersDisposable = speakersRepository.getSpeakersPage(pageNumber)
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
        Log.e(simpleClassName, "Something went wrong with fetching next speakers page for SpeakersViewModel", throwable)
    }

    private fun createErrorState(): State.Error {
        return State.Error(::onErrorClick)
    }

    private fun onErrorClick() {
        val previousEvents = mergeWithExistingSpeakers(listOf(State.Loading))
        speakersSubject.onNext(previousEvents)
        nextPageNumber?.let { loadNextPage(it) }
    }

    override fun onCleared() {
        speakersDisposable?.dispose()
    }
}