package pl.droidsonroids.toast.test

import android.app.Instrumentation
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.uiautomator.UiDevice
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.function.getString
import pl.droidsonroids.toast.function.getStringWithoutFormattingArguments
import pl.droidsonroids.toast.robot.InfoDialogRobot

class InfoDialogTest {
    @JvmField
    @Rule
    val activityRule = IntentsTestRule(MainActivity::class.java, true, true)

    @Test
    fun isMenuOverflowDisplayedAndClickable() {
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.menuItemAbout)
            checkIfElementWithIdIsClickable(R.id.menuItemAbout)
        }
    }

    @Test
    fun isEveryElementOnInfoDialogDisplayed() {
        with(InfoDialogRobot()) {
            showDialog()
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
        with(InfoDialogRobot()) {
            showDialog()
            checkIfElementWithIdIsDisplayedInDialog(R.id.closeImageButton)
            performClickOnElementWithId(R.id.closeImageButton)
            isDialogClosed()
        }
    }

    @Test
    fun isDialogClosedAfterClickingOnBackButton() {
        with(InfoDialogRobot()) {
            showDialog()
            Espresso.pressBack()
            isDialogClosed()
        }
    }

    @Test
    fun isDialogNotDismissedAfterTappingOnDialog() {
        with(InfoDialogRobot()) {
            showDialog()
            performClickOnElementWithId(R.id.toastLogoImage)
            checkIfElementWithIdIsDisplayedInDialog(R.id.toastLogoImage)
        }
    }

    @Test
    fun isDialogDismissedAfterTappingOutsideDialog() {
        with(InfoDialogRobot()) {
            showDialog()
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(0, 300)
            isDialogClosed()
        }
    }

    @Test
    fun isFbFanPageDeepLinkDisplayedAndActive() {
        val expectedIntent = CoreMatchers.allOf(IntentMatchers.hasAction(CoreMatchers.equalTo(Intent.ACTION_VIEW)))
        with(InfoDialogRobot()) {
            showDialog()
            checkIfTextIsCorrect(getString(R.string.toast_facebook_fanpage), R.id.fanpageLinkText)
            Intents.intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))
            performClickOnElementWithId(R.id.fanpageLinkText)
            checkIfIntentOpensFacebook()
        }
    }
}