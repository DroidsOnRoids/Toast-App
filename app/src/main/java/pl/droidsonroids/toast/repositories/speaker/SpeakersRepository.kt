package pl.droidsonroids.toast.repositories.speaker

import io.reactivex.Single
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.utils.Constants

interface SpeakersRepository {

    fun getSpeakersPage(pageNumber: Int = Constants.FIRST_PAGE): Single<Page<SpeakerDto>>

    fun searchSpeakersPage(query: String, pageNumber: Int = Constants.FIRST_PAGE): Single<Page<SpeakerDto>>
}