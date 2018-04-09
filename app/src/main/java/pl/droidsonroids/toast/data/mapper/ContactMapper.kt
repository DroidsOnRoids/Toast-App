package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.contact.ApiMessage
import pl.droidsonroids.toast.data.db.DbMessage
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.enums.MessageType

fun MessageDto.toApi(): ApiMessage {
    return ApiMessage(
            email = email,
            type = type,
            name = name,
            message = message
    )
}

fun MessageDto.toDb(): DbMessage {
    return DbMessage(
            email = email,
            type = type,
            name = name,
            message = message
    )
}

fun DbMessage.toDto(): MessageDto {
    return MessageDto(
            email = email ?: "",
            type = type ?: MessageType.I_WANT_TO,
            name = name ?: "",
            message = message ?: ""
    )
}