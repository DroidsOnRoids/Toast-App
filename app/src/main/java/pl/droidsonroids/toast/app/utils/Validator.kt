package pl.droidsonroids.toast.app.utils

import pl.droidsonroids.toast.utils.Constants


object Validator {

    fun getEmailError(emailInput: CharSequence): String? {
        return when {
            emailInput.isEmpty() -> "Name is empty"
            !emailInput.matches(Regex(Constants.ValidationRegex.EMAIL)) -> "Invalid email"
            else -> null
        }
    }

    fun getMessageError(messageInput: CharSequence): String? {
        return when {
            messageInput.isEmpty() -> "Name is empty"
            messageInput.length < 40 -> "Message must have 40 characters at least"
            messageInput.length > 250 -> "Message should not have more than 250 characters"
            else -> null
        }
    }

    fun getNameError(nameInput: CharSequence): String? {
        return when {
            nameInput.isEmpty() -> "Name is empty"
            !nameInput.matches(Regex(Constants.ValidationRegex.NAME)) -> "Invalid name"
            else -> null
        }
    }
}