package pl.droidsonroids.toast.repositories.contact

import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.mapper.toApi
import pl.droidsonroids.toast.services.ContactService
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(private val contactService: ContactService) : ContactRepository {
    override fun sendMessage(message: MessageDto) = contactService.sendMessage(message.toApi())
}