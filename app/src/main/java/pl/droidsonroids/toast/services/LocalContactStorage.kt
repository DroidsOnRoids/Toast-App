package pl.droidsonroids.toast.services

import android.content.SharedPreferences
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import javax.inject.Inject
import javax.inject.Singleton

private const val MESSAGE_TYPE_KEY = "message_type_key"
private const val MESSAGE_EMAIL_KEY = "message_email_key"
private const val MESSAGE_NAME_KEY = "message_name_key"
private const val MESSAGE_KEY = "message_key"

@Singleton
class LocalContactStorage @Inject constructor(private val sharedPreferences: SharedPreferences) : ContactStorage {

    override fun saveMessage(messageDto: MessageDto) {
        sharedPreferences.edit().apply {
            putString(MESSAGE_TYPE_KEY, messageDto.type)
            putString(MESSAGE_EMAIL_KEY, messageDto.email)
            putString(MESSAGE_NAME_KEY, messageDto.name)
            putString(MESSAGE_KEY, messageDto.message)
        }.apply()
    }

    override fun readMessage(): Single<MessageDto> {
        return Single.fromCallable {
            MessageDto(
                    email = sharedPreferences.getString(MESSAGE_EMAIL_KEY, ""),
                    type = sharedPreferences.getString(MESSAGE_TYPE_KEY, ""),
                    name = sharedPreferences.getString(MESSAGE_NAME_KEY, ""),
                    message = sharedPreferences.getString(MESSAGE_KEY, "")
            )
        }.subscribeOn(Schedulers.io())
    }
}