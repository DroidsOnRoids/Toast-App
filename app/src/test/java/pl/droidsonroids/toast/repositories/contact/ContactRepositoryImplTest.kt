package pl.droidsonroids.toast.repositories.contact

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.enums.MessageType
import pl.droidsonroids.toast.data.mapper.toDb
import pl.droidsonroids.toast.services.ContactService
import pl.droidsonroids.toast.services.ContactStorage
import java.io.IOException

class ContactRepositoryImplTest : RxTestBase() {
    @Mock
    lateinit var contactStorage: ContactStorage
    @Mock
    lateinit var contactService: ContactService
    @InjectMocks
    lateinit var contactRepository: ContactRepositoryImpl

    private val messageDto = MessageDto(
            email = "john@example.test",
            type = MessageType.TALK,
            name = "John",
            message = "test message"
    )

    @Test
    fun shouldSendMessage() {
        whenever(contactService.sendMessage(any())).thenReturn(Completable.complete())

        contactRepository.sendMessage(messageDto)
                .test()
                .assertComplete()
    }

    @Test
    fun shouldSendMessageReturnError() {
        whenever(contactService.sendMessage(any())).thenReturn(Completable.error(IOException()))

        contactRepository.sendMessage(messageDto)
                .test()
                .assertError(IOException::class.java)
    }

    @Test
    fun shouldSaveMessage() {
        contactRepository.saveMessage(messageDto)

        verify(contactStorage).saveMessage(eq(messageDto.toDb()))
    }

    @Test
    fun shouldReadMessage() {
        whenever(contactStorage.readMessage()).thenReturn(Single.just(messageDto.toDb()))
        contactRepository.readMessage()
                .test()
                .assertValue(messageDto)
    }
}