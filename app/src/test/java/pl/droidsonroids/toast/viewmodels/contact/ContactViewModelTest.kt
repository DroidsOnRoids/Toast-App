package pl.droidsonroids.toast.viewmodels.contact

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.repositories.contact.ContactRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import java.io.IOException

class ContactViewModelTest : RxTestBase() {
    @Mock
    lateinit var contactRepository: ContactRepository
    @InjectMocks
    lateinit var contactViewModel: ContactViewModel

    private val name = "John"
    private val email = "john@example.test"
    private val topic = 1
    private val topicType = "TALK"
    private val message = "test message"
    private val messageDto = MessageDto(
            email = email,
            type = topicType,
            name = name,
            message = message
    )

    @Before
    fun setUp() {
        contactViewModel.topic.set(topic)
        contactViewModel.name.set(name)
        contactViewModel.email.set(email)
        contactViewModel.message.set(message)
    }

    @Test
    fun shouldSentMessage() {
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(Completable.complete())
        val testObserver = contactViewModel.navigationSubject.test()

        contactViewModel.onSendClick()

        testObserver.assertValue { it is NavigationRequest.MessageSent }
        assertThat(contactViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
        assertThatFieldsAreCleared()
    }

    private fun assertThatFieldsAreCleared() {
        assertThat(contactViewModel.topic.get(), equalTo(0))
        assertThat(contactViewModel.name.get(), equalTo(""))
        assertThat(contactViewModel.email.get(), equalTo(""))
        assertThat(contactViewModel.message.get(), equalTo(""))
    }

    @Test
    fun shouldReturnError() {
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(Completable.error(IOException()))

        contactViewModel.onSendClick()

        assertThat(contactViewModel.loadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }

    @Test
    fun shouldRetrySendingMessage() {
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(Completable.error(IOException()))
        contactViewModel.onSendClick()
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(Completable.complete())
        val testObserver = contactViewModel.navigationSubject.test()

        contactViewModel.retryLoading()

        testObserver.assertValue { it is NavigationRequest.MessageSent }
        assertThat(contactViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
        assertThatFieldsAreCleared()
    }
}