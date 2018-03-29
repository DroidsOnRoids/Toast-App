package pl.droidsonroids.toast.repositories.contact

import io.reactivex.Single
import pl.droidsonroids.toast.data.db.DbMessage
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.mapper.toApi
import pl.droidsonroids.toast.data.mapper.toDb
import pl.droidsonroids.toast.data.mapper.toDto
import pl.droidsonroids.toast.services.ContactService
import pl.droidsonroids.toast.services.ContactStorage
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(private val contactService: ContactService, private val contactStorage: ContactStorage) : ContactRepository {
    override fun sendMessage(message: MessageDto) = contactService.sendMessage(message.toApi())

    override fun saveMessage(message: MessageDto) = contactStorage.saveMessage(message.toDb())

    override fun readMessage(): Single<MessageDto> = contactStorage.readMessage().map(DbMessage::toDto)
}
