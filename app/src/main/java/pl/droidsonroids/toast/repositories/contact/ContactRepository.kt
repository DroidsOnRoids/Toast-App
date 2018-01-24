package pl.droidsonroids.toast.repositories.contact

import io.reactivex.Completable
import io.reactivex.Single
import pl.droidsonroids.toast.data.dto.contact.MessageDto

interface ContactRepository {
    fun sendMessage(message: MessageDto): Completable
    fun saveMessage(message: MessageDto)
    fun readMessage(): Single<MessageDto>
}