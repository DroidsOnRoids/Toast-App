package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.repositories.contact.ContactRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

private const val I_WANT_TO_POSITION = 0
private const val TALK_POSITION = 1
private const val TALK = "TALK"
private const val REWARD_POSITION = 2
private const val REWARD = "REWARD"
private const val PARTNER_POSITION = 3
private const val PARTNER = "PARTNER"

class ContactViewModel @Inject constructor(private val contactRepository: ContactRepository) : ViewModel(), LoadingViewModel, NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.SUCCESS)

    val topic: ObservableField<Int> = ObservableField()

    val name: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    val message: ObservableField<String> = ObservableField("")
    private var sendDisposable: Disposable = Disposables.disposed()

    override fun retryLoading() = onSendClick()

    fun onSendClick() {
        val message = createMessageDto()
        loadingStatus.set(LoadingStatus.PENDING)
        sendDisposable = contactRepository.sendMessage(message)
                .subscribeBy(
                        onComplete = (::onSendSuccessfully),
                        onError = { loadingStatus.set(LoadingStatus.ERROR) }
                )
    }

    private fun createMessageDto(): MessageDto {
        val type = resolveMessageType()
        return MessageDto(
                email = email.get(),
                type = type,
                name = name.get(),
                message = message.get()
        )
    }

    private fun resolveMessageType(): String {
        return when (topic.get()) {
            TALK_POSITION -> TALK
            REWARD_POSITION -> REWARD
            PARTNER_POSITION -> PARTNER
            else -> "" // TODO: TOA-90 throw Exception here, validation shouldn't allow getting here if there's a different topic
        }
    }

    private fun onSendSuccessfully() {
        clearAllFields()
        navigationSubject.onNext(NavigationRequest.MessageSent)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun clearAllFields() {
        topic.set(I_WANT_TO_POSITION)
        name.set("")
        email.set("")
        message.set("")
    }

    override fun onCleared() {
        sendDisposable.dispose()
    }
}