package pl.droidsonroids.toast.data.mapper

import pl.droidsonroids.toast.data.api.contact.ApiMessage
import pl.droidsonroids.toast.data.dto.contact.MessageDto

fun MessageDto.toApi(): ApiMessage {
    return ApiMessage(
            email = email,
            type = type,
            name = name,
            message = message
    )
}