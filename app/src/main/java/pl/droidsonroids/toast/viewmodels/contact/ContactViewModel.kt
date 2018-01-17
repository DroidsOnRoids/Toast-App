package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.ObservableField
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.app.utils.Validator
import pl.droidsonroids.toast.data.MessageType
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.repositories.contact.ContactRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class ContactViewModel @Inject constructor(
        private var validator: Validator,
        private val contactRepository: ContactRepository
) : ViewModel(), LoadingViewModel, NavigatingViewModel {

    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.SUCCESS)

    val sendingEnabled = ObservableField(false)
    val nameInputError = ObservableField("")
    val emailInputError = ObservableField("")
    val messageInputError = ObservableField("")

    val selectedTopicPosition = ObservableField(0)
    val name: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    val message: ObservableField<String> = ObservableField("")
    private var sendDisposable: Disposable = Disposables.disposed()

    init {
        updateSendingEnabled()
        addSelectedTopicPositionListener()
    }

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

    fun nameChanged(nameInput: CharSequence, start: Int, before: Int, count: Int) {
        val error = validator.getNameError(nameInput)
        nameInputError.set(error)
        updateSendingEnabled()
    }

    fun emailChanged(emailInput: CharSequence, start: Int, before: Int, count: Int) {
        val error = validator.getEmailError(emailInput)
        emailInputError.set(error)
        updateSendingEnabled()
    }

    fun messageChanged(messageInput: CharSequence, start: Int, before: Int, count: Int) {
        val error = validator.getMessageError(messageInput)
        messageInputError.set(error)
        updateSendingEnabled()
    }

    private fun updateSendingEnabled() {
        sendingEnabled.set(isFormNotEmpty() && isTopicSelected())
    }

    private fun addSelectedTopicPositionListener() {
        selectedTopicPosition.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                updateSendingEnabled()
            }
        })
    }

    private fun isTopicSelected() = selectedTopicPosition.get() != 0

    private fun isFormNotEmpty() =
            arrayOf(name, email, message).all { it.get().isNotEmpty() }

    private fun createMessageDto(): MessageDto {
        val type = resolveMessageType()
        return MessageDto(
                email = email.get(),
                type = type,
                name = name.get(),
                message = message.get()
        )
    }

    private fun resolveMessageType(): MessageType {
        return MessageType[selectedTopicPosition.get()]
    }

    private fun onSendSuccessfully() {
        clearAllFields()
        navigationSubject.onNext(NavigationRequest.MessageSent)
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun clearAllFields() {
        selectedTopicPosition.set(MessageType.I_WANT_TO.ordinal)
        name.set("")
        email.set("")
        message.set("")
    }

    override fun onCleared() {
        sendDisposable.dispose()
    }
}
