package pl.droidsonroids.toast.viewmodels.contact

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.utils.ContactFormValidator
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.enums.MessageType
import pl.droidsonroids.toast.repositories.contact.ContactRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.DelayViewModel
import java.io.IOException

class ContactViewModelTest : RxTestBase() {
    @Mock
    lateinit var contactRepository: ContactRepository
    @Mock
    lateinit var contactFormValidator: ContactFormValidator
    @Mock
    lateinit var analyticsEventTracker: AnalyticsEventTracker
    @Mock
    lateinit var delayViewModel: DelayViewModel

    lateinit var contactViewModel: ContactViewModel

    private val name = "John"
    private val email = "john@example.test"
    private val topic = MessageType.TALK
    private val message = "test message"
    private val messageDto = MessageDto(
            email = email,
            type = topic,
            name = name,
            message = message
    )

    @Before
    fun setUp() {
        whenever(contactRepository.readMessage()).thenReturn(Single.just(messageDto))
        contactViewModel = ContactViewModel(contactFormValidator, contactRepository, analyticsEventTracker, delayViewModel)
    }

    @Test
    fun shouldSentMessage() {
        val complete = Completable.complete()
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(complete)
        whenever(delayViewModel.addLoadingDelay(any())).thenReturn(complete)
        val testObserver = contactViewModel.navigationSubject.test()

        contactViewModel.onSendClick()

        testObserver.assertValue { it is NavigationRequest.MessageSent }
        assertThat(contactViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
    }

    @Test
    fun shouldClearFieldsAfterMessageSent() {
        val complete = Completable.complete()
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(complete)
        whenever(delayViewModel.addLoadingDelay(any())).thenReturn(complete)

        contactViewModel.onSendClick()

        assertThatFieldsAreCleared()
    }

    private fun assertThatFieldsAreCleared() {
        assertThat(contactViewModel.selectedTopicPosition.get(), equalTo(0))
        assertThat(contactViewModel.name.get(), equalTo(""))
        assertThat(contactViewModel.email.get(), equalTo(""))
        assertThat(contactViewModel.message.get(), equalTo(""))
    }

    @Test
    fun shouldReturnError() {
        val error = Completable.error(IOException())
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(error)
        whenever(delayViewModel.addLoadingDelay(any())).thenReturn(error)

        contactViewModel.onSendClick()

        assertThat(contactViewModel.loadingStatus.get(), equalTo(LoadingStatus.ERROR))
    }

    @Test
    fun shouldRetrySendingMessage() {
        val error = Completable.error(IOException())
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(error)
        whenever(delayViewModel.addLoadingDelay(any())).thenReturn(error)
        contactViewModel.onSendClick()

        val complete = Completable.complete()
        whenever(contactRepository.sendMessage(eq(messageDto))).thenReturn(complete)
        whenever(delayViewModel.addLoadingDelay(complete)).thenReturn(complete)
        val testObserver = contactViewModel.navigationSubject.test()

        contactViewModel.retryLoading()

        testObserver.assertValue { it is NavigationRequest.MessageSent }
        assertThat(contactViewModel.loadingStatus.get(), equalTo(LoadingStatus.SUCCESS))
        assertThatFieldsAreCleared()
    }

    @Test
    fun shouldSaveFieldsState() {
        contactViewModel.saveMessage()
        verify(contactRepository).saveMessage(messageDto)
    }

    @Test
    fun shouldRestoreFieldsState() {
        assertThat(contactViewModel.selectedTopicPosition.get(), equalTo(topic.ordinal))
        assertThat(contactViewModel.email.get(), equalTo(email))
        assertThat(contactViewModel.message.get(), equalTo(message))
        assertThat(contactViewModel.name.get(), equalTo(name))
    }

}