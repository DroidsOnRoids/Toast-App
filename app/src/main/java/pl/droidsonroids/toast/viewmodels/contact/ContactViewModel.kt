package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.app.utils.ContactFormValidator
import pl.droidsonroids.toast.app.utils.callbacks.OnPropertyChangedSkippableCallback
import pl.droidsonroids.toast.app.utils.extensions.unicodeLength
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.enums.MessageType
import pl.droidsonroids.toast.repositories.contact.ContactRepository
import pl.droidsonroids.toast.utils.LoadingStatus
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.addOnPropertyChangedCallback
import pl.droidsonroids.toast.viewmodels.DelayViewModel
import pl.droidsonroids.toast.viewmodels.LoadingViewModel
import pl.droidsonroids.toast.viewmodels.NavigatingViewModel
import javax.inject.Inject

class ContactViewModel @Inject constructor(
        private val contactFormValidator: ContactFormValidator,
        private val contactRepository: ContactRepository,
        private val analyticsEventTracker: AnalyticsEventTracker,
        delayViewModel: DelayViewModel
) : ViewModel(), LoadingViewModel, DelayViewModel by delayViewModel, NavigatingViewModel {

    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val loadingStatus: ObservableField<LoadingStatus> = ObservableField(LoadingStatus.PENDING)
    override val isFadingEnabled get() = true

    val sendingEnabled = ObservableField(false)

    val nameInputError = ObservableField<String?>()
    val emailInputError = ObservableField<String?>()
    val messageInputError = ObservableField<String?>()

    val selectedTopicPosition = ObservableField(0)
    val name: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    val message: ObservableField<String> = ObservableField("")
    val messageCounter: ObservableField<String> = ObservableField("0 / 250")

    private val nameChangedCallback = OnPropertyChangedSkippableCallback { onTextChanged(name) }
    private val emailChangedCallback = OnPropertyChangedSkippableCallback { onTextChanged(email) }
    private val messageChangedCallback = OnPropertyChangedSkippableCallback { onTextChanged(message) }

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

    private fun onTextChanged(property: ObservableField<String>) {
        when (property) {
            name -> nameInputError.set(contactFormValidator.getNameError(property.get()!!))
            email -> emailInputError.set(contactFormValidator.getEmailError(property.get()!!))
            message -> updateMessage(property.get()!!)
        }
        updateSendingEnabled()
    }

    private fun updateMessage(message: String) {
        messageInputError.set(contactFormValidator.getMessageError(message))
        messageCounter.set("${message.unicodeLength} / 250")
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
        sendingEnabled.set(contactFormValidator.isFormValid(
                errors = listOf(nameInputError, emailInputError, messageInputError).map { it.get() },
                inputs = listOf(name, email, message).map { it.get()!! },
                topicPosition = selectedTopicPosition.get()!!
        ))
    }

    private fun addSelectedTopicPositionListener() {
        selectedTopicPosition.addOnPropertyChangedCallback {
            updateSendingEnabled()
            analyticsEventTracker.logContactChooseTopicEvent(resolveMessageType().name)
        }
    }

    override fun retryLoading() = onSendClick()

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun onSendClick() {
        val message = createMessageDto()
        loadingStatus.set(LoadingStatus.PENDING)
        updateLastLoadingStartTime()
        compositeDisposable += contactRepository.sendMessage(message)
                .let(::addLoadingDelay)
                .subscribeBy(
                        onComplete = ::onSendSuccessfully,
                        onError = { loadingStatus.set(LoadingStatus.ERROR) }
                )
        analyticsEventTracker.logContactSendClickEvent(message.type.name)
    }

    private fun createMessageDto(): MessageDto {
        val type = resolveMessageType()
        return MessageDto(
                email = email.get()!!,
                type = type,
                name = name.get()!!,
                message = message.get()!!
        )
    }

    private fun resolveMessageType(): MessageType {
        return MessageType[selectedTopicPosition.get()!!]
    }

    private fun onSendSuccessfully() {
        clearForm()
        saveMessage()
        clearErrors()
        showSuccessDialog()
        skipInputChangeCallbacks()
        loadingStatus.set(LoadingStatus.SUCCESS)
    }

    private fun clearForm() {
        selectedTopicPosition.set(MessageType.I_WANT_TO.ordinal)
        name.set("")
        email.set("")
        message.set("")
    }

    fun saveMessage() {
        contactRepository.saveMessage(createMessageDto())
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
}
