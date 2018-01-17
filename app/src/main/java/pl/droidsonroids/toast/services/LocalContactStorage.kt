package pl.droidsonroids.toast.services

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.toast.data.MessageType
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import javax.inject.Inject
import javax.inject.Singleton

private const val MESSAGE_KEY = "message"

@Singleton
class LocalContactStorage @Inject constructor(private val sharedPreferences: SharedPreferences) : ContactStorage {
    private val gson by lazy { Gson() }

    override fun saveMessage(messageDto: MessageDto) {
        sharedPreferences.edit().apply {
            putString(MESSAGE_KEY, gson.toJson(messageDto))
        }.apply()
    }

    override fun readMessage(): Single<MessageDto> {
        return Single.fromCallable {
            val message = sharedPreferences.getString(MESSAGE_KEY, null)
            gson.fromJson(message, MessageDto::class.java)
                    ?: MessageDto(email = "", type = MessageType.I_WANT_TO, name = "", message = "")
        }.subscribeOn(Schedulers.io())
    }
}