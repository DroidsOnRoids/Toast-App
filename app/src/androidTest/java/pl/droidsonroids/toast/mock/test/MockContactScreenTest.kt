package pl.droidsonroids.toast.mock.test

import android.support.test.espresso.intent.rule.IntentsTestRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import pl.droidsonroids.testing.mockwebserver.FixtureDispatcher
import pl.droidsonroids.testing.mockwebserver.condition.PathQueryConditionFactory
import pl.droidsonroids.toast.app.home.MainActivity


class MockContactScreenTest {
    @JvmField
    @Rule
    val activityRule = IntentsTestRule(MainActivity::class.java, true, false)

    val mockWebServer = MockWebServer()

    @Before
    fun setup() {
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
        dispatcher.putResponse(factory.withPathInfix("/events"), "events_200")
        dispatcher.putResponse(factory.withPathInfix("/events/16"), "event16_200")
        mockWebServer.setDispatcher(dispatcher)
    }
}