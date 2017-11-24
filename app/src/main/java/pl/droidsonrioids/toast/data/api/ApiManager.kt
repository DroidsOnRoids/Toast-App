package pl.droidsonrioids.toast.data.api

import io.reactivex.Single
import pl.droidsonrioids.toast.data.model.Event

interface ApiManager {
    fun getEvents(): Single<List<Event>>
    fun getEvent(id: Int): Single<Event>
}