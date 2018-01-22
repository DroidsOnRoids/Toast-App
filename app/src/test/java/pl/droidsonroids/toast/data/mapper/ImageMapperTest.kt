package pl.droidsonroids.toast.data.mapper

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.droidsonroids.toast.testImageDto

class ImageMapperTest {
    private val position = 1L

    @Test
    fun shouldMapImageDtoToItemViewModel() {
        val action: (Long) -> Unit = mock()
        val photoItemViewModel = testImageDto.toItemViewModel(position, action)
        assertThat(photoItemViewModel.image, equalTo(testImageDto))
        assertThat(photoItemViewModel.position, equalTo(position))
        photoItemViewModel.onClick()
        verify(action).invoke(position)
    }

    @Test
    fun shouldMapImageDtoToSingleViewModel() {
        val photoLoadedCallback: () -> Unit = mock()
        val singlePhotoViewModel = testImageDto.toSingleViewModel(position, photoLoadedCallback)
        assertThat(singlePhotoViewModel.image, equalTo(testImageDto))
        assertThat(singlePhotoViewModel.position, equalTo(position))
        assertThat(singlePhotoViewModel.photoLoadedCallback, equalTo(photoLoadedCallback))
    }
}