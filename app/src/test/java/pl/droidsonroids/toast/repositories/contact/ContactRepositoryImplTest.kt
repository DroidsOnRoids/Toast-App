package pl.droidsonroids.toast.repositories.contact

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.services.ContactService
import java.io.IOException

class ContactRepositoryImplTest : RxTestBase() {
    @Mock
    lateinit var contactService: ContactService
    @InjectMocks
    lateinit var contactRepository: ContactRepositoryImpl

    @Test
    fun shouldSendMessage() {
        whenever(contactService.sendMessage(any())).thenReturn(Completable.complete())

        val messageDto = MessageDto(
                email = "john@example.test",
                type = "TALK",
                name = "John",
                message = "test message"
        )
        contactRepository.sendMessage(messageDto)
                .test()
                .assertComplete()
    }

    @Test
    fun shouldReturnError() {
        whenever(contactService.sendMessage(any())).thenReturn(Completable.error(IOException()))

        val messageDto = MessageDto(
                email = "john@example.test",
                type = "TALK",
                name = "John",
                message = "test message"
        )
        contactRepository.sendMessage(messageDto)
                .test()
                .assertError(IOException::class.java)
    }

}