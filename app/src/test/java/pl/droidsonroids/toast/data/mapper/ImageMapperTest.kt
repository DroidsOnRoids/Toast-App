package pl.droidsonroids.toast.data.mapper

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.testImageDto

class ImageMapperTest {
    private val position = 1

    @Test
    fun shouldMapImageDtoToItemViewModel() {
        val action: (Int) -> Unit = mock()
        val photoItemViewModel = testImageDto.toItemViewModel(position, action)
        assertThat(photoItemViewModel.image, equalTo(testImageDto))
        assertThat(photoItemViewModel.position, equalTo(position))
        photoItemViewModel.onClick()
        verify(action).invoke(position)
    }

    @Test
    fun shouldMapImageDtoToFullscreenViewModel() {
        val onClick: () -> Unit = mock()
        val fullscreenPhotoViewModel = testImageDto.toFullscreenViewModel(position, onClick)
        assertThat(fullscreenPhotoViewModel.image, equalTo(testImageDto))
        assertThat(fullscreenPhotoViewModel.position, equalTo(position))
        fullscreenPhotoViewModel.onClick()
        verify(onClick).invoke()
    }
}