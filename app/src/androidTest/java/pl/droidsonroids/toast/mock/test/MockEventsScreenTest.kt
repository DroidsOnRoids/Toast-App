package pl.droidsonroids.toast.mock.test

import android.support.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import pl.droidsonroids.testing.mockwebserver.FixtureDispatcher
import pl.droidsonroids.testing.mockwebserver.condition.PathQueryConditionFactory
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.function.getString
import pl.droidsonroids.toast.robot.EventsRobot

class MockEventsScreenTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

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

    @Test
    fun isToolbarDisplayed() {
        val toolbarTitle = getString(R.string.events_title)
        with(EventsRobot()) {
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }

    @Test
    fun isEveryElementDisplayedProperlyOnUpcomingEventCard() {
        with(EventsRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.topBar)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventImage)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventTitleText)
            checkIfTextIsCorrect(getString(R.string.android_developers_meet_up), R.id.upcomingEventDescriptionText)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventLocationImage)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventPlaceTitleText)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventPlaceLocationText)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventCalendarImage)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventDateText)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventClockImage)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventTimeText)
        }
    }

    @Test
    fun isEveryDividerDisplayedOnUpcomingEventCard() {
        with(EventsRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventTitleDivider)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventLocationDivider)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventTimeDivider)
            checkIfElementWithIdIsDisplayed(R.id.upcomingEventDateDivider)
        }
    }

    private fun setPathDispatcher() {
        val dispatcher = FixtureDispatcher()
        val factory = PathQueryConditionFactory("")
        dispatcher.putResponse(factory.withPathInfix("/events"), "events_200")
        dispatcher.putResponse(factory.withPathInfix("/events/16"), "event16_200")
        mockWebServer.setDispatcher(dispatcher)
    }
}