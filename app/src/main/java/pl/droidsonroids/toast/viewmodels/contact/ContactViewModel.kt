package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.text.Editable
import pl.droidsonroids.toast.utils.Constants
import javax.inject.Inject


class ContactViewModel @Inject constructor() : ViewModel() {
    val areInputsValid = ObservableField<Boolean>()
    val selectedTopicPosition = ObservableField<Int>()
    val nameInputError = ObservableField<String>()
    val emailInputError = ObservableField<String>()
    val messageInputError = ObservableField<String>()

    private var nameInputText: String
    private var emailInputText: String
    private var messageInputText: String

    init {
        nameInputText = ""
        emailInputText = ""
        messageInputText = ""
        areInputsValid.set(isFormValid() && isFormNotEmpty())
    }

    fun nameChanged(editable: Editable) {
        nameInputText = editable.toString()
        when {
            editable.isEmpty() -> nameInputError.set("Name is empty")
            !editable.matches(Regex(Constants.ValidationRegex.NAME)) -> nameInputError.set("Invalid name")
            else -> nameInputError.set(null)
        }
        areInputsValid.set(isFormValid() && isFormNotEmpty())
    }

    fun emailChanged(editable: Editable) {
        emailInputText = editable.toString()
        when {
            editable.isEmpty() -> emailInputError.set("Name is empty")
            !editable.matches(Regex(Constants.ValidationRegex.EMAIL)) -> emailInputError.set("Invalid email")
            else -> emailInputError.set(null)
        }
        areInputsValid.set(isFormValid() && isFormNotEmpty())
    }

    fun messageChanged(editable: Editable) {
        messageInputText = editable.toString()
        when {
            editable.isEmpty() -> messageInputError.set("Name is empty")
            editable.length < 40 -> messageInputError.set("Message must have 40 characters at least")
            editable.length > 250 -> messageInputError.set("Message should not have more than 250 characters")
            else -> messageInputError.set(null)
        }
        areInputsValid.set(isFormValid() && isFormNotEmpty())
    }

    private fun isFormValid() =
            isObservableErrorNull(nameInputError) &&
                    isObservableErrorNull(emailInputError) &&
                    isObservableErrorNull(messageInputError)


    private fun isFormNotEmpty() =
            nameInputText.isNotEmpty() && emailInputText.isNotEmpty() && messageInputText.isNotEmpty()

    private fun isObservableErrorNull(error: ObservableField<String>) = error.get() == null

}