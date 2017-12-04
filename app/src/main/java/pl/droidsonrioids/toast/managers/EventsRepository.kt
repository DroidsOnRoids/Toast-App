package pl.droidsonrioids.toast.managers

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonrioids.toast.data.dto.EventDetailsDto
import pl.droidsonrioids.toast.data.dto.SplitEvents

interface EventsRepository {

    fun getEvents(): Maybe<SplitEvents>

    fun getEvent(id: Long): Single<EventDetailsDto>
}