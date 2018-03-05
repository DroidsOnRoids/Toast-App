package pl.droidsonroids.toast.mock.test

import android.support.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import pl.droidsonroids.testing.mockwebserver.FixtureDispatcher
import pl.droidsonroids.testing.mockwebserver.condition.PathQueryConditionFactory
import pl.droidsonroids.toast.app.home.MainActivity


class MockEventDetailsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        setPathDispatcher()
        mockWebServer.start(12345)
        activityRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun setPathDispatcher() {
        val dispatcher = FixtureDispatcher()
        val factory = PathQueryConditionFactory("")
        dispatcher.putResponse(factory.withPathInfix("/events"), "events17_200")
        dispatcher.putResponse(factory.withPathInfix("/events/17"), "event17_200")
        dispatcher.putResponse(factory.withPathInfix("/speakers"), "speakers_200")
        dispatcher.putResponse(factory.withPathInfix("/speakers/16"), "speakers16_200")
        mockWebServer.setDispatcher(dispatcher)
    }
}
