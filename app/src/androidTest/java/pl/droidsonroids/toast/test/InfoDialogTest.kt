package pl.droidsonroids.toast.test

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.uiautomator.UiDevice
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

    private fun showDialog() {
        with(InfoDialogRobot()) {
            performClickOnElementWithId(R.id.menuItemAbout)
        }
    }

    private fun isDialogClosed() {
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsNotPresentInHierarchy(R.id.toastLogoImage)
        }
    }

    @Test
    fun isMenuOverflowDisplayed() {
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.menuItemAbout)
        }
    }

    @Test
    fun isMenuOverflowClickable() {
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsClickable(R.id.menuItemAbout)
        }
    }

    @Test
    fun isToastLogoDisplayed() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsDisplayedInDialog(R.id.toastLogoImage)
        }
    }

    @Test
    fun isHeartImageDisplayed() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsDisplayedInDialog(R.id.hearthImage)
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
    fun isCreatedWithTextDisplayed() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfTextIsCorrect(getString(R.string.created_with), R.id.createdWithText)
        }
    }

    @Test
    fun isByToastTeamTextDisplayed() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfTextIsCorrect(getString(R.string.by_toast_team), R.id.byToastTeamText)
        }
    }

    @Test
    fun isMoreInfoTextDisplayed() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfTextIsCorrect(getString(R.string.for_more_information_visit_our), R.id.moreInfoText)
        }
    }

    @Test
    fun isAppVersionTextDisplayed() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfTextStartsWith(getStringWithoutFormattingArguments(R.string.application_version), R.id.appVersionText)
        }
    }

    @Test
    fun isFbFanPageDeepLinkDisplayedAndActive() {
        showDialog()
        with(InfoDialogRobot()) {
            checkIfTextIsCorrect(getString(R.string.toast_facebook_fanpage), R.id.fanpageLinkText)
            performClickOnElementWithId(R.id.fanpageLinkText)
            checkIfIntentOpensFacebook()
        }
    }
}