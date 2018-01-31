package pl.droidsonroids.toast.data.mapper

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.data.api.ApiImage
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.api.speaker.ApiSpeakerDetails
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto

class SpeakersMapperTest {
    @Test
    fun shouldMapApiSpeakerToDto() {
        val id = 1L
        val name = "name"
        val job = "job"
        val avatar = ApiImage("bigImageUrl", "thumbImageUrl")
        val apiSpeaker = ApiSpeaker(id, name, job, avatar)
        val speakerDto = apiSpeaker.toDto()

        assertThat(speakerDto.id, equalTo(id))
        assertThat(speakerDto.name, equalTo(name))
        assertThat(speakerDto.job, equalTo(job))
        assertThat(speakerDto.avatar, equalTo(avatar.toDto()))
    }

    @Test
    fun shouldMapApiSpeakerDetailsToDto() {
        val id = 1L
        val name = "name"
        val job = "job"
        val bio = "bio"
        val github = "github"
        val email = "email"
        val website = "website"
        val twitter = "twitter"
        val avatar = ApiImage("bigImageUrl", "thumbImageUrl")
        val apiSpeaker = ApiSpeakerDetails(
                id = id,
                name = name,
                job = job,
                avatar = avatar,
                bio = bio,
                github = github,
                website = website,
                twitter = twitter,
                email = email)
        val speakerDto = apiSpeaker.toDto()

        assertThat(speakerDto.id, equalTo(id))
        assertThat(speakerDto.name, equalTo(name))
        assertThat(speakerDto.job, equalTo(job))
        assertThat(speakerDto.bio, equalTo(bio))
        assertThat(speakerDto.avatar, equalTo(avatar.toDto()))
    }

    @Test
    fun shouldMapSpeakerDtoToViewModel() {
        val id = 1L
        val name = "name"
        val job = "job"
        val avatar = ImageDto("bigImageUrl", "thumbImageUrl")
        val onClick: (Long) -> Unit = mock()
        val speakerDto = SpeakerDto(id, name, job, avatar)
        val speakerItemViewModel = speakerDto.toViewModel(onClick)

        assertThat(speakerItemViewModel.id, equalTo(id))
        assertThat(speakerItemViewModel.name, equalTo(name))
        assertThat(speakerItemViewModel.job, equalTo(job))
        assertThat(speakerItemViewModel.avatar, equalTo(avatar))
        speakerItemViewModel.onClick()
        verify(onClick).invoke(id)
    }
}