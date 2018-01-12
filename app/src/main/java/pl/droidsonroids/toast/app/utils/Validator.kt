package pl.droidsonroids.toast.app.utils

import pl.droidsonroids.toast.utils.Constants

object Validator {
    private const val MIN_MESSAGE_LENGTH = 40
    private const val MAX_MESSAGE_LENGTH = 250
    private const val MIN_MESSAGE_LENGTH_ERROR = "Message must have 40 characters at least"
    private const val MAX_MESSAGE_LENGTH_ERROR = "Message should not have more than 250 characters"
    private const val EMPTY_MESSAGE_ERROR = "Message is empty"

    private const val INVALID_EMAIL_ERROR = "Invalid email"
    private const val EMPTY_EMAIL_ERROR = "Email is empty"

    private const val INVALID_NAME_ERROR = "Invalid name"
    private const val EMPTY_NAME_ERROR = "Name is empty"

    fun getEmailError(emailInput: CharSequence): String? {
        return when {
            emailInput.isEmpty() -> EMPTY_EMAIL_ERROR
            !emailInput.matches(Regex(Constants.ValidationRegex.EMAIL)) -> INVALID_EMAIL_ERROR
            else -> null
        }
    }

    fun getMessageError(messageInput: CharSequence): String? {
        return when {
            messageInput.isEmpty() -> EMPTY_MESSAGE_ERROR
            messageInput.length < MIN_MESSAGE_LENGTH -> MIN_MESSAGE_LENGTH_ERROR
            messageInput.length > MAX_MESSAGE_LENGTH -> MAX_MESSAGE_LENGTH_ERROR
            else -> null
        }
    }

    fun getNameError(nameInput: CharSequence): String? {
        return when {
            nameInput.isEmpty() -> EMPTY_NAME_ERROR
            !nameInput.matches(Regex(Constants.ValidationRegex.NAME)) -> INVALID_NAME_ERROR
            else -> null
        }
    }
}