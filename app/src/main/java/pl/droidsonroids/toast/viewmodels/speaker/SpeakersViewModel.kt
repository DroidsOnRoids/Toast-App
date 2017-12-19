package pl.droidsonroids.toast.viewmodels.speaker

import android.databinding.ObservableField
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import javax.inject.Inject

class SpeakersViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : BaseSpeakerListViewModel() {
    val isSortingDetailsVisible: ObservableField<Boolean> = ObservableField(false)

    private var speakersDisposable: Disposable? = null

    init {
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

    fun loadNextPage() {
        val nextPageNumber = this.nextPageNumber
        if (!isNextPageLoading && nextPageNumber != null) {
            loadPage(nextPageNumber)
        }
    }

    private fun loadPage(pageNumber: Int) {
        isNextPageLoading = true
        speakersDisposable = speakersRepository.getSpeakersPage(pageNumber)
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

    override fun onCleared() {
        speakersDisposable?.dispose()
    }
}