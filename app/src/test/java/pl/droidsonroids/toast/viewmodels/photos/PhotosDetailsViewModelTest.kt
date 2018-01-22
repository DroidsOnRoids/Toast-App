package pl.droidsonroids.toast.viewmodels.photos

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.testImageDto

class PhotosDetailsViewModelTest : RxTestBase() {

    private lateinit var photosDetailsViewModel: PhotosDetailsViewModel
    private val testPhotos = listOf(testImageDto)
    private val position = 0L

    @Before
    fun setUp() {
        photosDetailsViewModel = PhotosDetailsViewModel()
        photosDetailsViewModel.init(testPhotos)
    }

    @Test
    fun shouldReturnSinglePhotoViewModel() {
        with(photosDetailsViewModel.singlePhotoViewModels) {
            assertThat(size, equalTo(1))
            assertThat(first().image, equalTo(testImageDto))
            assertThat(first().position, equalTo(position))
        }
    }

    @Test
    fun shouldPostPhotoIdToPhotoLoadedSubject() {
        val testObserver = photosDetailsViewModel.photoLoadedSubject.test()
        photosDetailsViewModel.singlePhotoViewModels.first().photoLoadedCallback()
        testObserver.assertValue { it == position }
    }
}