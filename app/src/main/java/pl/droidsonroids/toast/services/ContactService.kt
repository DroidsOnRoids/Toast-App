package pl.droidsonroids.toast.services

import io.reactivex.Completable
import pl.droidsonroids.toast.data.api.contact.ApiMessage
import retrofit2.http.Body
import retrofit2.http.POST

interface ContactService {
    @POST("contact")
    fun sendMessage(@Body message: ApiMessage): Completable
}