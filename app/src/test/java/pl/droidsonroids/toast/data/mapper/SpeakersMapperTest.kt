package pl.droidsonroids.toast.data.mapper

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.data.api.ApiImage
import pl.droidsonroids.toast.data.api.speaker.ApiSpeaker
import pl.droidsonroids.toast.data.dto.ImageDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto
import pl.droidsonroids.toast.data.dto.speaker.SpeakerTalkDto
import pl.droidsonroids.toast.testApiSpeakerDetails
import pl.droidsonroids.toast.testApiSpeakerTalk
import pl.droidsonroids.toast.testSpeakerTalkDto

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
        val speakerDto = testApiSpeakerDetails.toDto()

        with(testApiSpeakerDetails) {
            assertThat(speakerDto.id, equalTo(id))
            assertThat(speakerDto.name, equalTo(name))
            assertThat(speakerDto.job, equalTo(job))
            assertThat(speakerDto.bio, equalTo(bio))
            assertThat(speakerDto.avatar, equalTo(avatar.toDto()))
            assertThat(speakerDto.talks, equalTo(talks.map { it.toDto() }))
        }
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

    @Test
    fun shouldMapApiSpeakerTalkToDto() {
        val (id, title, description, _) = testApiSpeakerTalk.toDto()
        assertThat(id, equalTo(testApiSpeakerTalk.id))
        assertThat(title, equalTo(testApiSpeakerTalk.title))
        assertThat(description, equalTo(testApiSpeakerTalk.description))
    }

    @Test
    fun shouldMapSpeakerTalkDtoToViewModel() {
        val onReadMoreClick: (SpeakerTalkDto) -> Unit = mock()
        val onEventClick: (Long) -> Unit = mock()
        val viewModel = testSpeakerTalkDto.toViewModel(onReadMoreClick, onEventClick)
        viewModel.let {
            assertThat(it.id, equalTo(testSpeakerTalkDto.id))
            assertThat(it.title, equalTo(testSpeakerTalkDto.title))
            assertThat(it.description, equalTo(testSpeakerTalkDto.description))
            it.onReadMore()
            verify(onReadMoreClick).invoke(eq(it.toDto()))
        }
    }

    @Test
    fun shouldMapSpeakerTalkViewModelToDto() {
        val speakerTalkViewModel = testSpeakerTalkDto.toViewModel(mock(), mock())
        val (id, title, description, _) = speakerTalkViewModel.toDto()
        assertThat(id, equalTo(speakerTalkViewModel.id))
        assertThat(title, equalTo(speakerTalkViewModel.title))
        assertThat(description, equalTo(speakerTalkViewModel.description))
    }
}