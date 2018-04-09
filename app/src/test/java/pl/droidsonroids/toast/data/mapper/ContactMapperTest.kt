package pl.droidsonroids.toast.data.mapper

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.db.DbMessage
import pl.droidsonroids.toast.data.dto.contact.MessageDto
import pl.droidsonroids.toast.data.enums.MessageType

class ContactMapperTest : RxTestBase() {

    @Test
    fun shouldMapMessageDtoToApi() {
        val email = "john@example.test"
        val type = MessageType.TALK
        val name = "John"
        val message = "test message"
        val messageDto = MessageDto(
                email = email,
                type = type,
                name = name,
                message = message
        )
        val apiMessage = messageDto.toApi()
        assertThat(apiMessage.email, equalTo(messageDto.email))
        assertThat(apiMessage.type, equalTo(messageDto.type))
        assertThat(apiMessage.name, equalTo(messageDto.name))
        assertThat(apiMessage.message, equalTo(messageDto.message))
    }

    @Test
    fun shouldMapMessageDtoToDb() {
        val email = "john@example.test"
        val type = MessageType.TALK
        val name = "John"
        val message = "test message"
        val messageDto = MessageDto(
                email = email,
                type = type,
                name = name,
                message = message
        )
        val dbMessage = messageDto.toDb()
        assertThat(dbMessage.email, equalTo(messageDto.email))
        assertThat(dbMessage.type, equalTo(messageDto.type))
        assertThat(dbMessage.name, equalTo(messageDto.name))
        assertThat(dbMessage.message, equalTo(messageDto.message))
    }

    @Test
    fun shouldMapDbMessageToDto() {
        val email = "john@example.test"
        val type = MessageType.TALK
        val name = "John"
        val message = "test message"
        val dbMessage = DbMessage(
                email = email,
                type = type,
                name = name,
                message = message
        )
        val messageDto = dbMessage.toDto()
        assertThat(messageDto.email, equalTo(dbMessage.email))
        assertThat(messageDto.type, equalTo(dbMessage.type))
        assertThat(messageDto.name, equalTo(dbMessage.name))
        assertThat(messageDto.message, equalTo(dbMessage.message))
    }

    @Test
    fun shouldMapEmptyMessageToDefaultDto() {
        val dbMessage = DbMessage()
        val messageDto = dbMessage.toDto()
        assertThat(messageDto.name, equalTo(""))
        assertThat(messageDto.type, equalTo(MessageType.I_WANT_TO))
        assertThat(messageDto.name, equalTo(""))
        assertThat(messageDto.message, equalTo(""))
    }
}