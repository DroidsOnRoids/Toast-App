package pl.droidsonroids.toast.repositories

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.dto.EventDetailsDto
import pl.droidsonroids.toast.data.dto.EventDto
import pl.droidsonroids.toast.data.dto.SplitEvents

interface EventsRepository {

    fun getEvents(): Maybe<SplitEvents>

    fun getEvent(id: Long): Single<EventDetailsDto>
    fun getEventsPage(pageNumber: Int): Single<Page<EventDto>>
}