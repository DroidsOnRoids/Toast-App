package pl.droidsonroids.toast.repositories.contact

import io.reactivex.Single
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.mapper.toApi
import pl.droidsonroids.toast.services.ContactService
import pl.droidsonroids.toast.services.LocalContactService
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(private val contactService: ContactService, private val localContactService: LocalContactService) : ContactRepository {
    override fun sendMessage(message: MessageDto) = contactService.sendMessage(message.toApi())

    override fun saveMessage(message: MessageDto) = localContactService.saveMessage(message)

    override fun readMessage(): Single<MessageDto> = localContactService.readMessage()
}