package pl.droidsonroids.toast.data.dto.contact

import pl.droidsonroids.toast.data.MessageType

data class MessageDto(
        val email: String,
        val type: MessageType,
        val name: String,
        val message: String
)