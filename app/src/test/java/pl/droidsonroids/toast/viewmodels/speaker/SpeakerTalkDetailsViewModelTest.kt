package pl.droidsonroids.toast.viewmodels.speaker

import android.databinding.ObservableField
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import pl.droidsonroids.toast.RxTestBase
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.testSpeakerTalkDto
import pl.droidsonroids.toast.utils.NavigationRequest


class SpeakerTalkDetailsViewModelTest : RxTestBase() {
    @Mock
    private lateinit var analyticsEventTracker: AnalyticsEventTracker
    @Spy
    private var rotation = ObservableField(0f)
    @InjectMocks
    private lateinit var speakerTalkDetailsViewModel: SpeakerTalkDetailsViewModel

    @Before
    fun setUp() {
        speakerTalkDetailsViewModel.init(testSpeakerTalkDto, onCoverLoadingFinished = mock())
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