package pl.droidsonrioids.toast.data.api

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidsonrioids.toast.data.model.Event
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val SERVER_ADDRESS = "https://api.letswift.pl/api/v1/"
private const val TIMEOUT_SECONDS = 10L

class ApiManagerImpl : ApiManager {

    private val apiService: ApiService

    init {
        val httpClient = OkHttpClient.Builder().build()

        apiService = Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient)
                .build()
                .create(ApiService::class.java)
    }

    override fun getEvents(): Single<List<Event>> {
        return apiService
                .getEvents()
    }

    override fun getEvent(id: Int): Single<Event> {
        return apiService
                .getEvent(id)
    }

}