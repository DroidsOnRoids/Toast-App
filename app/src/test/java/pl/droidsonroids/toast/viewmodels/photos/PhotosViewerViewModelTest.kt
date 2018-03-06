package pl.droidsonroids.toast.viewmodels.photos

import android.databinding.ObservableField
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.testImageDto

class PhotosViewerViewModelTest : RxTestBase() {

    private lateinit var photosViewerViewModel: PhotosViewerViewModel
    private val testPhotos = listOf(testImageDto)
    private val position = 0L

    @Before
    fun setUp() {
        photosViewerViewModel = PhotosViewerViewModel(ObservableField(0f))
        photosViewerViewModel.init(testPhotos)
    }

    @Test
    fun shouldReturnSinglePhotoViewModel() {
        with(photosViewerViewModel.singlePhotoViewModels) {
            assertThat(size, equalTo(1))
            assertThat(first().image, equalTo(testImageDto))
            assertThat(first().position, equalTo(position))
        }
    }

    @Test
    fun shouldPostPhotoIdToPhotoLoadingFinishedSubject() {
        val testObserver = photosViewerViewModel.photoLoadingFinishedSubject.test()
        photosViewerViewModel.singlePhotoViewModels.first().onPhotoLoadingFinished()
        testObserver.assertValue { it == position }
    }
}