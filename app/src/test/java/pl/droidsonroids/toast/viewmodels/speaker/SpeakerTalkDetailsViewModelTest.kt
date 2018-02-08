package pl.droidsonroids.toast.viewmodels.speaker

import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonroids.toast.testSpeakerTalkDto
import pl.droidsonroids.toast.utils.NavigationRequest


@RunWith(MockitoJUnitRunner::class)
class SpeakerTalkDetailsViewModelTest {

    private val speakerTalkDetailsViewModel = SpeakerTalkDetailsViewModel()

    @Before
    fun setUp() {
        speakerTalkDetailsViewModel.init(testSpeakerTalkDto, mock())
    }

    @Test
    fun shouldRequestNavigationToClose() {
        val testObserver = speakerTalkDetailsViewModel.navigationSubject.test()
        speakerTalkDetailsViewModel.onReadLess()

        testObserver.assertValue {
            it == NavigationRequest.Close
        }
    }

    @Test
    fun shouldRequestNavigationToEventDetails() {
        val testObserver = speakerTalkDetailsViewModel.navigationSubject.test()
        speakerTalkDetailsViewModel.eventItemViewModel.get().onClick()

        testObserver.assertValue {
            it is NavigationRequest.EventDetails
                    && it.id == speakerTalkDetailsViewModel.eventItemViewModel.get().id
        }
    }

}