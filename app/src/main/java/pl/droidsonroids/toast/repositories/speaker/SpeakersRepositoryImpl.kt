package pl.droidsonroids.toast.repositories.speaker

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.services.SpeakerService
import pl.droidsonroids.toast.utils.toPage
import javax.inject.Inject

class SpeakersRepositoryImpl @Inject constructor(private val speakerService: SpeakerService) : SpeakersRepository {

    override fun getSpeakersPage(pageNumber: Int): Single<Page<SpeakerDto>> {
        return speakerService.getSpeakers(pageNumber = pageNumber)
                .flatMap { (apiSpeakers, allPagesCount) ->
                    apiSpeakers.toObservable()
                            .map { it.toDto() }
                            .toPage(pageNumber, allPagesCount)
                }
    }

    override fun searchSpeakersPage(query: String, pageNumber: Int): Single<Page<SpeakerDto>> {
        return speakerService.searchSpeakers(query = query, pageNumber = pageNumber)
                .flatMap { (apiSpeakers, allPagesCount) ->
                    apiSpeakers.toObservable()
                            .map { it.toDto() }
                            .toPage(pageNumber, allPagesCount)
                }
    }
}
