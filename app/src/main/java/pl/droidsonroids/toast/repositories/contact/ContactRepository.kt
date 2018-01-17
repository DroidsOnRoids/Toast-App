package pl.droidsonroids.toast.repositories.contact

import io.reactivex.Completable
import pl.droidsonroids.toast.data.dto.contact.MessageDto

interface ContactRepository {
    fun sendMessage(message: MessageDto): Completable
}