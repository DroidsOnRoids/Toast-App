package pl.droidsonrioids.toast.repositories

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.dto.EventDetailsDto
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.data.dto.SplitEvents
import pl.droidsonrioids.toast.data.model.Page

interface EventsRepository {

    fun getEvents(): Maybe<SplitEvents>

    fun getEvent(id: Long): Single<EventDetailsDto>
    fun getEventsPage(page: Int): Single<Page<EventDto>>
}