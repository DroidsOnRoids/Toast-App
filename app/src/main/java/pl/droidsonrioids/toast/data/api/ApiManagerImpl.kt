package pl.droidsonrioids.toast.data.api

import javax.inject.Inject

class ApiManagerImpl @Inject constructor(private val apiService: ApiService) : ApiManager {

    override fun getEvents() =
            apiService.getEvents()

    override fun getEvent(id: Int) =
            apiService.getEvent(id)

}