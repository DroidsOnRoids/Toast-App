package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.ObservableField
import pl.droidsonroids.toast.app.utils.Validator
import javax.inject.Inject

class ContactViewModel @Inject constructor() : ViewModel() {
    @Inject lateinit var validator: Validator

    val sendingEnabled = ObservableField<Boolean>()
    val nameInputError = ObservableField("")
    val emailInputError = ObservableField("")
    val messageInputError = ObservableField("")

    val selectedTopicPosition = ObservableField(0)
    val name: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    val message: ObservableField<String> = ObservableField("")

    init {
        updateSendingEnabled()
        addSelectedTopicPositionListener()
    }

    fun nameChanged(nameInput: CharSequence, start: Int, before: Int, count: Int) {
        name.set(nameInput.toString())
        val error = validator.getNameError(nameInput)
        nameInputError.set(error)
        updateSendingEnabled()
    }

    fun emailChanged(emailInput: CharSequence, start: Int, before: Int, count: Int) {
        email.set(emailInput.toString())
        val error = validator.getEmailError(emailInput)
        emailInputError.set(error)
        updateSendingEnabled()
    }

    fun messageChanged(messageInput: CharSequence, start: Int, before: Int, count: Int) {
        message.set(messageInput.toString())
        val error = validator.getMessageError(messageInput)
        messageInputError.set(error)
        updateSendingEnabled()
    }

    private fun updateSendingEnabled() {
        sendingEnabled.set(isFormValid() && isFormNotEmpty() && isTopicSelected())
    }

    private fun addSelectedTopicPositionListener() {
        selectedTopicPosition.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                updateSendingEnabled()
            }
        })
    }

    private fun isTopicSelected() = selectedTopicPosition.get() != 0

    private fun isFormValid() =
            isObservableErrorNull(nameInputError) &&
                    isObservableErrorNull(emailInputError) &&
                    isObservableErrorNull(messageInputError)

    private fun isFormNotEmpty() =
            arrayOf(name, email, message).all { it.get().isNotEmpty() }

    private fun isObservableErrorNull(error: ObservableField<String>) = error.get().isNotEmpty()

}