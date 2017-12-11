package pl.droidsonrioids.toast.repositories

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.Page
import pl.droidsonrioids.toast.data.dto.EventDetailsDto
import pl.droidsonrioids.toast.data.dto.EventDto
import pl.droidsonrioids.toast.data.dto.SplitEvents

interface EventsRepository {

    fun getEvents(): Maybe<SplitEvents>

    fun getEvent(id: Long): Single<EventDetailsDto>
    fun getEventsPage(pageNumber: Int): Single<Page<EventDto>>
}