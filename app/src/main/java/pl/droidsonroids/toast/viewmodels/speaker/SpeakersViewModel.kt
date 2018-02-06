package pl.droidsonroids.toast.viewmodels.speaker

import android.databinding.Observable
import android.databinding.ObservableField
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.repositories.speaker.SpeakersRepository
import pl.droidsonroids.toast.utils.Constants.Sorting.ALPHABETICAL
import pl.droidsonroids.toast.utils.Constants.Sorting.DATE
import pl.droidsonroids.toast.utils.LoadingStatus
import javax.inject.Inject

class SpeakersViewModel @Inject constructor(private val speakersRepository: SpeakersRepository) : BaseSpeakerListViewModel() {
    val isSortingDetailsVisible: ObservableField<Boolean> = ObservableField(false)
    private val sortingType = ObservableField(ALPHABETICAL)

    private var speakersDisposable: Disposable? = null

    init {
        loadFirstPage()
        sortingType.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(p0: Observable?, p1: Int) {
                clearSpeakersList()
                loadFirstPage()
            }
        })
    }

    private fun clearSpeakersList() {
        onNewSpeakersPageLoaded(Page(emptyList(), 0, 0))
    }

    fun toggleSortingDetailsVisibility() {
        isSortingDetailsVisible.set(!isSortingDetailsVisible.get())
    }

    fun onAlphabeticalSortingClick() {
        sortingType.set(ALPHABETICAL)
    }

    fun onDateSortingClick() {
        sortingType.set(DATE)
    }

    override fun retryLoading() {
        loadFirstPage()
    }

    private fun loadFirstPage() {
        loadingStatus.set(LoadingStatus.PENDING)
        speakersDisposable = speakersRepository.getSpeakersPage(sortingType = sortingType.get())
                .flatMap(::mapToSingleSpeakerItemViewModelsPage)
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
        speakersDisposable = speakersRepository.getSpeakersPage(pageNumber, sortingType.get())
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