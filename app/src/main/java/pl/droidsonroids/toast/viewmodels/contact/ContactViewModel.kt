package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
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
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.PENDING)

    val topic: ObservableField<Int> = ObservableField()

    val name: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    val message: ObservableField<String> = ObservableField("")
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun retryLoading() = onSendClick()

    init {
        compositeDisposable += contactRepository.readMessage()
                .subscribe { it ->
                    setMessage(it)
                    loadingStatus.set(LoadingStatus.SUCCESS)
                }
    }

    private fun setMessage(messageDto: MessageDto) {
        messageDto.let {
            val typePosition = resolveMessageTypePosition(it.type)
            topic.set(typePosition)
            name.set(it.name)
            email.set(it.email)
            message.set(it.message)
        }
    }

    private fun resolveMessageTypePosition(type: String): Int {
        return when (type) {
            TALK -> TALK_POSITION
            REWARD -> REWARD_POSITION
            PARTNER -> PARTNER_POSITION
            else -> I_WANT_TO_POSITION
        }
    }

    fun saveMessage() {
        contactRepository.saveMessage(createMessageDto())
    }

    fun onSendClick() {
        val message = createMessageDto()
        loadingStatus.set(LoadingStatus.PENDING)
        compositeDisposable += contactRepository.sendMessage(message)
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
            else -> ""
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
        saveMessage()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}