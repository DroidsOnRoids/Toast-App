package pl.droidsonroids.toast.app.utils

import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.StringProvider
import javax.inject.Inject

private const val MIN_MESSAGE_LENGTH = 40
private const val MAX_MESSAGE_LENGTH = 250

class Validator @Inject constructor(private val stringProvider: StringProvider) {

    fun getEmailError(emailInput: CharSequence): String? {
        return when {
            emailInput.isEmpty() -> stringProvider.getString(R.string.empty_email_error)
            !emailInput.matches(Constants.ValidationPatterns.EMAIL.toRegex()) -> stringProvider.getString(R.string.invalid_email)
            else -> null
        }
    }

    fun getMessageError(messageInput: CharSequence): String? {
        return when {
            messageInput.isEmpty() -> stringProvider.getString(R.string.empty_message_error)
            messageInput.getUnicodeLength() < MIN_MESSAGE_LENGTH -> stringProvider.getString(R.string.min_message_length_error)
            messageInput.getUnicodeLength() > MAX_MESSAGE_LENGTH -> stringProvider.getString(R.string.max_message_length_error)
            else -> null
        }
    }

    fun getNameError(nameInput: CharSequence): String? {
        return when {
            nameInput.isEmpty() -> stringProvider.getString(R.string.empty_name_error)
            !nameInput.matches(Constants.ValidationPatterns.NAME.toRegex()) -> stringProvider.getString(R.string.invalid_name)
            else -> null
        }
    }

}