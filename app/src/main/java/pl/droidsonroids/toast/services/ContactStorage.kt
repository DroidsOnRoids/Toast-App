package pl.droidsonroids.toast.services

import io.reactivex.Single
import pl.droidsonroids.toast.data.dto.contact.MessageDto

interface ContactStorage {
    fun saveMessage(messageDto: MessageDto)
    fun readMessage(): Single<MessageDto>
}