package pl.droidsonroids.toast.viewmodels.speaker

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.State
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.data.mapper.toViewModel
import pl.droidsonroids.toast.data.wrapWithState
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.toPage
import pl.droidsonroids.toast.viewmodels.LoadingViewModel

abstract class BaseSpeakerListViewModel : ViewModel(), LoadingViewModel {
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.SUCCESS)
    val speakersSubject: BehaviorSubject<List<State<SpeakerItemViewModel>>> = BehaviorSubject.create()
    protected var isNextPageLoading: Boolean = false
    protected var nextPageNumber: Int? = null
    private val Any.simpleClassName: String get() = javaClass.simpleName

    protected fun mapToSingleSpeakerItemViewModelsPage(page: Page<SpeakerDto>): Single<Page<State.Item<SpeakerItemViewModel>>> {
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

    protected fun onSpeakersPageLoaded(page: Page<State.Item<SpeakerItemViewModel>>) {
        val speakers = getSpeakers(page)
        speakersSubject.onNext(speakers)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun getSpeakers(page: Page<State.Item<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
        val speakers = mergeWithExistingSpeakers(page.items)
        return speakers.addLoadingIfNextPageAvailable(page)
    }

    protected fun List<State<SpeakerItemViewModel>>.addLoadingIfNextPageAvailable(page: Page<State.Item<SpeakerItemViewModel>>): List<State<SpeakerItemViewModel>> {
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
                ?: listOf()
        return previousList + newList
    }

    protected fun onFirstPageLoadError(throwable: Throwable) {
        speakersSubject.onNext(listOf())
        loadingStatus.set(LoadingStatus.ERROR)
        Log.e(simpleClassName, "Something went wrong with fetching data for SpeakersSearchViewModel", throwable)
    }

    protected fun onNextPageLoadError(throwable: Throwable) {
        val speakers = mergeWithExistingSpeakers(listOf(createErrorState()))
        speakersSubject.onNext(speakers)
        Log.e(simpleClassName, "Something went wrong with fetching next speakers page for SpeakersSearchViewModel", throwable)
    }

    private fun createErrorState(): State.Error {
        return State.Error(::onErrorClick)
    }

    abstract protected fun onErrorClick()
}