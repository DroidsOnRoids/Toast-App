package pl.droidsonroids.toast

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonroids.toast.rule.RxPluginSchedulerRule

@RunWith(MockitoJUnitRunner::class)
abstract class RxTestBase {
    @get:Rule
    val rxPluginSchedulerRule = RxPluginSchedulerRule()

}