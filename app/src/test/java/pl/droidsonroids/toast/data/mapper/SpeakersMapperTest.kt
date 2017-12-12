package pl.droidsonroids.toast.data.mapper

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.data.api.ApiImage
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker

class SpeakersMapperTest {
    @Test
    fun shouldMapApiSpeakerToDto() {
        val id = 1L
        val name = "name"
        val job = "job"
        val avatar = ApiImage("bigImageUrl", "thumbImageUrl")
        val bio = "bio"
        val apiSpeaker = ApiSpeaker(id, name, job, avatar, bio)
        val speakerDto = apiSpeaker.toDto()

        assertThat(speakerDto.id, equalTo(id))
        assertThat(speakerDto.name, equalTo(name))
        assertThat(speakerDto.job, equalTo(job))
        assertThat(speakerDto.avatar, equalTo(avatar.toDto()))
    }
}