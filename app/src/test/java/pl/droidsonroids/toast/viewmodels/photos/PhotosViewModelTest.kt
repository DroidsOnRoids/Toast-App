package pl.droidsonroids.toast.viewmodels.photos

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.testImageDto
import pl.droidsonroids.toast.utils.NavigationRequest

class PhotosViewModelTest : RxTestBase() {
    private lateinit var photosViewModel: PhotosViewModel
    private val testPhotos = listOf(testImageDto)
    private val position = 0


    @Before
    fun setUp() {
        photosViewModel = PhotosViewModel()
        photosViewModel.init(testPhotos)
    }

    @Test
    fun shouldReturnPhotoViewModel() {
        with(photosViewModel.photosSubject.value) {
            assertThat(size, equalTo(1))
            assertThat(first().image, equalTo(testImageDto))
            assertThat(first().position, equalTo(position))
        }
    }


    @Test
    fun shouldReturnFullscreenPhotoViewModel() {
        with(photosViewModel.fullscreenPhotosSubject.value) {
            assertThat(size, equalTo(1))
            assertThat(first().image, equalTo(testImageDto))
            assertThat(first().position, equalTo(position))
        }
    }

    @Test
    fun shouldRequestToggleImmersive() {
        val testObserver = photosViewModel.navigationSubject.test()
        photosViewModel.onFullscreenPhotoClick()

        testObserver.assertValue {
            it == NavigationRequest.ToggleImmersive
        }

    }

    @Test
    fun shouldRequestFullscreenPhoto() {
        val testObserver = photosViewModel.navigationSubject.test()
        photosViewModel.onPhotoItemClicked(position)

        testObserver.assertValue {
            it == NavigationRequest.FullscreenPhoto(position)
        }

    }
}