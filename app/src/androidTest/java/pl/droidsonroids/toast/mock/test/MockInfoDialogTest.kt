package pl.droidsonroids.toast.mock.test

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.uiautomator.UiDevice
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import pl.droidsonroids.testing.mockwebserver.FixtureDispatcher
import pl.droidsonroids.testing.mockwebserver.condition.PathQueryConditionFactory
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.function.getString
import pl.droidsonroids.toast.function.getStringWithoutFormattingArguments
import pl.droidsonroids.toast.robot.InfoDialogRobot
import android.app.Instrumentation
import android.support.test.espresso.intent.Intents.intending
import android.content.Intent
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf

class MockInfoDialogTest {

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

    private fun showDialog() {
        with(InfoDialogRobot()) {
            openMenuOverflow()
            performClickOnElementWithText(getString(R.string.about_app))
        }
    }

    private fun openMenuOverflow() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    private fun isDialogClosed() {
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsNotPresentInHierarchy(R.id.toastLogoImage)
        }
    }

    @Test
    fun isMenuOverflowDisplayed() {
        openMenuOverflow()
        with(InfoDialogRobot()) {
            checkIfElementWithTextIsDisplayed(getString(R.string.about_app))
        }
    }

    @Test
    fun isMenuOverflowClickable() {
        openMenuOverflow()
        with(InfoDialogRobot()) {
            checkIfElementWithTextIsClickable(getString(R.string.about_app))
        }
    }

    @Test
    fun isEveryElementOnInfoDialogDisplayed() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsDisplayedInDialog(R.id.toastLogoImage)
            checkIfElementWithIdIsDisplayedInDialog(R.id.hearthImage)
            checkIfTextIsCorrect(getString(R.string.created_with), R.id.createdWithText)
            checkIfTextIsCorrect(getString(R.string.by_toast_team), R.id.byToastTeamText)
            checkIfTextIsCorrect(getString(R.string.for_more_information_visit_our), R.id.moreInfoText)
            checkIfTextStartsWith(getStringWithoutFormattingArguments(R.string.application_version), R.id.appVersionText)
        }
    }

    /*
    Using workaround to check if Dialog is closed as Dialog does not have ID
    Test is checking if Toast Logo (visible on Dialog) is NOT displayed
     */

    @Test
    fun isDialogClosedAfterClickingOnCloseButton() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsDisplayedInDialog(R.id.closeImageButton)
            performClickOnElementWithId(R.id.closeImageButton)
            isDialogClosed()
        }
    }

    @Test
    fun isDialogClosedAfterClickingOnBackButton() {
        showDialog()
        Espresso.pressBack()
        isDialogClosed()
    }

    @Test
    fun isDialogNotDismissedAfterTappingOnDialog() {
        showDialog()
        with(InfoDialogRobot()) {
            performClickOnElementWithId(R.id.toastLogoImage)
            checkIfElementWithIdIsDisplayedInDialog(R.id.toastLogoImage)
        }
    }

    @Test
    fun isDialogDismissedAfterTappingOutsideDialog() {
        showDialog()
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(0, 300)
        isDialogClosed()
    }

    @Test
    fun isFbFanPageDeepLinkDisplayedAndActive() {
        val expectedIntent = allOf(hasAction(CoreMatchers.equalTo(Intent.ACTION_VIEW)))
        showDialog()
        with(InfoDialogRobot()) {
            checkIfTextIsCorrect(getString(R.string.toast_facebook_fanpage), R.id.fanpageLinkText)
            intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))
            performClickOnElementWithId(R.id.fanpageLinkText)
            checkIfIntentOpensFacebook()
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