package pl.droidsonroids.toast.repositories

import io.reactivex.Single
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto

interface SpeakersRepository {

    fun getSpeakersPage(pageNumber: Int): Single<Page<SpeakerDto>>
}