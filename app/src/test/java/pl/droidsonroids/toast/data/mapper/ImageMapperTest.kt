package pl.droidsonroids.toast.data.mapper

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.data.dto.ImageDto

class ImageMapperTest {
    @Test
    fun shouldMapImageDtoToViewModel() {
        val testImageDto = ImageDto(
                "originalSizeUrl",
                "thumbSizeUrl"
        )
        val action: () -> Unit = mock()
        val photoItemViewModel = testImageDto.toViewModel(action)
        assertThat(photoItemViewModel.image, equalTo(testImageDto))
        photoItemViewModel.onClick()
        verify(action).invoke()
    }
}