package pl.droidsonroids.toast.repositories

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonroids.toast.data.Page
import pl.droidsonroids.toast.data.dto.event.EventDetailsDto
import pl.droidsonroids.toast.data.dto.event.EventDto
import pl.droidsonroids.toast.data.dto.event.SplitEvents

interface EventsRepository {

    fun getEvents(): Maybe<SplitEvents>

    fun getEvent(id: Long): Single<EventDetailsDto>
    fun getEventsPage(pageNumber: Int): Single<Page<EventDto>>
}