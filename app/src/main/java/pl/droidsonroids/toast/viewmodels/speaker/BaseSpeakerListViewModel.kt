package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.data.wrapWithState
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.toPage
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import timber.log.Timber

abstract class BaseSpeakerListViewModel : ViewModel(), LoadingViewModel, NavigatingViewModel {
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.SUCCESS)
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    val speakersSubject: BehaviorSubject<List<State<SpeakerItemViewModel>>> = BehaviorSubject.create()
    protected var isNextPageLoading: Boolean = false
    protected var nextPageNumber: Int? = null

    protected fun mapToSingleSpeakerItemViewModelsPage(page: Page<SpeakerDto>): Single<Page<State.Item<SpeakerItemViewModel>>> {
        val (items, pageNumber, allPagesCount) = page
        return items.toObservable()
                .map {
                    it.toViewModel { id, name ->
                        navigationSubject.onNext(NavigationRequest.SpeakerDetails(id))
                        onSpeakerNavigationRequestSent(name)
                    }
                }
                .map { wrapWithState(it) }
                .toPage(pageNumber, allPagesCount)
    }

    protected fun onSpeakersPageLoaded(page: Page<State.Item<SpeakerItemViewModel>>) {
        val speakers = getSpeakers(page)
        speakersSubject.onNext(speakers)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    protected open fun onNewSpeakersPageLoaded(page: Page<State.Item<SpeakerItemViewModel>>) {
        val speakers = page.items.appendLoadingItemIfNextPageAvailable(page)
        speakersSubject.onNext(speakers)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun getSpeakers(page: Page<State.Item<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
        val speakers = mergeWithExistingSpeakers(page.items)
        return speakers.appendLoadingItemIfNextPageAvailable(page)
    }

    private fun List<State<SpeakerItemViewModel>>.appendLoadingItemIfNextPageAvailable(page: Page<State.Item<SpeakerItemViewModel>>)
            : List<State<SpeakerItemViewModel>> {
        return if (page.pageNumber < page.allPagesCount) {
            nextPageNumber = page.pageNumber + 1
            this + State.Loading
        } else {
            nextPageNumber = null
            this
        }
    }

    protected fun mergeWithExistingSpeakers(newList: List<State<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
        val previousList = speakersSubject.value
                ?.filter { it is State.Item }
                ?: emptyList()
        return previousList + newList
    }

    protected fun onFirstPageLoadError(throwable: Throwable) {
        speakersSubject.onNext(emptyList())
        loadingStatus.set(LoadingStatus.ERROR)
        Timber.e(throwable, "Something went wrong with fetching data for SpeakersListViewModel")
    }

    protected fun onNextPageLoadError(throwable: Throwable) {
        val speakers = mergeWithExistingSpeakers(listOf(createErrorState()))
        speakersSubject.onNext(speakers)
        Timber.e(throwable, "Something went wrong with fetching next speakers page for SpeakersListViewModel")
    }

    private fun createErrorState(): State.Error {
        return State.Error(::onErrorClick)
    }

    abstract fun onSpeakerNavigationRequestSent(speakerName: String)

    protected abstract fun onErrorClick()
}