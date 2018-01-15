package pl.droidsonroids.toast.app.utils

import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.StringResourceProvider
import javax.inject.Inject

private const val MIN_MESSAGE_LENGTH = 40
private const val MAX_MESSAGE_LENGTH = 250

class Validator @Inject constructor(private val stringResourceProvider: StringResourceProvider) {

    fun getEmailError(emailInput: CharSequence): String {
        return when {
            emailInput.isEmpty() -> stringResourceProvider.getString(R.string.empty_email_error)
            !emailInput.matches(Constants.ValidationPatterns.EMAIL.toRegex()) -> stringResourceProvider.getString(R.string.invalid_email)
            else -> ""
        }
    }

    fun getMessageError(messageInput: CharSequence): String {
        return when {
            messageInput.isEmpty() -> stringResourceProvider.getString(R.string.empty_message_error)
            getTextLength(messageInput.toString()) < MIN_MESSAGE_LENGTH -> stringResourceProvider.getString(R.string.min_message_length_error)
            getTextLength(messageInput.toString()) > MAX_MESSAGE_LENGTH -> stringResourceProvider.getString(R.string.max_message_length_error)
            else -> ""
        }
    }

    fun getNameError(nameInput: CharSequence): String {
        return when {
            nameInput.isEmpty() -> stringResourceProvider.getString(R.string.empty_name_error)
            !nameInput.matches(Constants.ValidationPatterns.NAME.toRegex()) -> stringResourceProvider.getString(R.string.invalid_name)
            else -> ""
        }
    }

    private fun getTextLength(text: String)
            = text.codePointCount(0, text.length)

}