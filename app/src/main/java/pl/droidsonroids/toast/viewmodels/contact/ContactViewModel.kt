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

    private val nameChangedCallback = OnPropertyChangedSkippableCallback(onTextChangedCallback(name))
    private val emailChangedCallback = OnPropertyChangedSkippableCallback(onTextChangedCallback(email))
    private val messageChangedCallback = OnPropertyChangedSkippableCallback(onTextChangedCallback(message))

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        updateSendingEnabled()
        addSelectedTopicPositionListener()
        addNameTextChangedListener()
        addEmailTextChangedListener()
        addMessageTextChangedListener()
        compositeDisposable += contactRepository.readMessage()
                .subscribe { it ->
                    setMessage(it)
                    loadingStatus.set(LoadingStatus.SUCCESS)
                }
    }

    private fun addNameTextChangedListener() {
        name.addOnPropertyChangedCallback(nameChangedCallback)
    }

    private fun addEmailTextChangedListener() {
        email.addOnPropertyChangedCallback(emailChangedCallback)
    }

    private fun addMessageTextChangedListener() {
        message.addOnPropertyChangedCallback(messageChangedCallback)
    }

    private fun onTextChangedCallback(property: ObservableField<String>): Observable.OnPropertyChangedCallback {
        return object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                when (property) {
                    name -> nameInputError.set(validator.getNameError(property.get()))
                    email -> emailInputError.set(validator.getEmailError(property.get()))
                    message -> messageInputError.set(validator.getMessageError(property.get()))
                }
                updateSendingEnabled()
            }
        }
    }

    private fun setMessage(messageDto: MessageDto) {
        messageDto.let {
            selectedTopicPosition.set(it.type.ordinal)
            name.set(it.name)
            email.set(it.email)
            message.set(it.message)
        }
    }

    private fun updateSendingEnabled() {
        sendingEnabled.set(validator.isFormValid(
                errors = listOf(nameInputError, emailInputError, messageInputError).map { it.get() },
                inputs = listOf(name, email, message).map { it.get() },
                topicPosition = selectedTopicPosition.get()
        ))
    }

    private fun addSelectedTopicPositionListener() {
        selectedTopicPosition.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                updateSendingEnabled()
            }
        })
    }

    override fun retryLoading() = onSendClick()

    override fun onCleared() {
        compositeDisposable.dispose()
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

    private fun resolveMessageType(): MessageType {
        return MessageType[selectedTopicPosition.get()]
    }

    private fun onSendSuccessfully() {
        clearForm()
        saveMessage()
        clearErrors()
        skipInputChangeCallbacks()
        showSuccessDialog()
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun clearForm() {
        selectedTopicPosition.set(MessageType.I_WANT_TO.ordinal)
        name.set("")
        email.set("")
        message.set("")
    }

    private fun clearErrors() {
        nameInputError.set(null)
        emailInputError.set(null)
        messageInputError.set(null)
    }

    private fun skipInputChangeCallbacks() {
        nameChangedCallback.skipNextInvocation = true
        emailChangedCallback.skipNextInvocation = true
        messageChangedCallback.skipNextInvocation = true
    }

    private fun showSuccessDialog() {
        navigationSubject.onNext(NavigationRequest.MessageSent)
    }

    inner class OnPropertyChangedSkippableCallback(private val callback: Observable.OnPropertyChangedCallback) : Observable.OnPropertyChangedCallback() {

        var skipNextInvocation = false

        override fun onPropertyChanged(observable: Observable?, propertyId: Int) {
            if (skipNextInvocation) {
                skipNextInvocation = false
            } else {
                callback.onPropertyChanged(observable, propertyId)
            }
        }

    }
}
