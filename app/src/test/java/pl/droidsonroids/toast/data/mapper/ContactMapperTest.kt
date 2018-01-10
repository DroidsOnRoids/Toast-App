package pl.droidsonroids.toast.data.mapper

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.data.dto.contact.MessageDto

class ContactMapperTest : RxTestBase() {

    @Test
    fun shouldMapMessageDtoToApi() {
        val email = "john@example.test"
        val type = "TALK"
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

}