package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
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
        private val validator: Validator,
        private val contactRepository: ContactRepository
) : ViewModel(), LoadingViewModel, NavigatingViewModel {

    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.PENDING)

    val sendingEnabled = ObservableField(false)
    val nameInputError = ObservableField<String?>(null)
    val emailInputError = ObservableField<String?>(null)
    val messageInputError = ObservableField<String?>(null)

    val selectedTopicPosition = ObservableField(0)
    val name: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    val message: ObservableField<String> = ObservableField("")
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        updateSendingEnabled()
        addSelectedTopicPositionListener()
        addNameTextChangedListener()
        addEmailTextChangedListener()
        addMessageTextChangedListener()
    }

    private fun addMessageTextChangedListener() {
        message.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val error = validator.getMessageError(message.get())
                messageInputError.set(error)
                updateSendingEnabled()
            }
        })
    }

    private fun addEmailTextChangedListener() {
        email.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val error = validator.getEmailError(email.get())
                emailInputError.set(error)
                updateSendingEnabled()
            }
        })
    }

    private fun addNameTextChangedListener() {
        name.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val error = validator.getNameError(name.get())
                nameInputError.set(error)
                updateSendingEnabled()
            }
        })
    }

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
            topic.set(it.type.ordinal)
            name.set(it.name)
            email.set(it.email)
            message.set(it.message)
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

    private fun updateSendingEnabled() {
        sendingEnabled.set(isFormValid && isFormNotEmpty && isTopicSelected)
    }

    private fun addSelectedTopicPositionListener() {
        selectedTopicPosition.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                updateSendingEnabled()
            }
        })
    }

    private val isFormValid
        get() = arrayOf(nameInputError, emailInputError, messageInputError).all { it.get().isNullOrEmpty() }

    private val isTopicSelected
        get() = selectedTopicPosition.get() != 0

    private val isFormNotEmpty
        get() = arrayOf(name, email, message).all { it.get().isNotEmpty() }

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
        saveMessage()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}
