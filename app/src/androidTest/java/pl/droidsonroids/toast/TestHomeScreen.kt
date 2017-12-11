package pl.droidsonroids.toast

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.pressBack
import android.support.test.rule.ActivityTestRule
import android.support.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.app.home.MainActivity


class TestHomeScreen {
    @Suppress("unused")
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    private fun showDialog() {
        HomeRobot().performClickOnElementWithId(R.id.menuItemAbout)
    }

    private fun isDialogClosed() {
        HomeRobot().checkIfElementWithIdIsNotPresentInHierarchy(R.id.toastLogoImage)
    }

    @Test
    fun isToolbarDisplayed() {
        val toolbarTitle = getString(R.string.events_title)
        HomeRobot().checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)

    }

    @Test
    fun isMenuOverflowDisplayed() {
        HomeRobot().checkIfElementWithIdIsDisplayed(R.id.menuItemAbout)
    }

    @Test
    fun isMenuOverflowClickable() {
        HomeRobot().checkIfElementWithIdIsClickable(R.id.menuItemAbout)
    }

    @Test
    fun isToastLogoDisplayed() {
        showDialog()
        HomeRobot().checkIfElementWithIdIsDisplayedInDialog(R.id.toastLogoImage)
    }

    @Test
    fun isHeartImageDisplayed() {
        showDialog()
        HomeRobot().checkIfElementWithIdIsDisplayedInDialog(R.id.hearthImage)
    }

    /*
    Using workaround to check if Dialog is closed as Dialog does not have ID
    Test is checking if Toast Logo (visible on Dialog) is NOT displayed
     */

    @Test
    fun isDialogClosedAfterClickingOnCloseButton() {
        showDialog()
        HomeRobot().checkIfElementWithIdIsDisplayedInDialog(R.id.closeImageButton)
        HomeRobot().performClickOnElementWithId(R.id.closeImageButton)
        isDialogClosed()
    }

    @Test
    fun isDialogClosedAfterClickingOnBackButton() {
        showDialog()
        pressBack()
        isDialogClosed()
    }

    @Test
    fun isDialogNotDismissedAfterTappingOnDialog() {
        showDialog()
        HomeRobot().performClickOnElementWithId(R.id.toastLogoImage)
        HomeRobot().checkIfElementWithIdIsDisplayedInDialog(R.id.toastLogoImage)
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
        HomeRobot().checkIfTextIsCorrect(getString(R.string.created_with), R.id.createdWithText)
    }

    @Test
    fun isByToastTeamTextDisplayed() {
        showDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.by_toast_team), R.id.byToastTeamText)
    }

    @Test
    fun isMoreInfoTextDisplayed() {
        showDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.for_more_information_visit_our), R.id.moreInfoText)
    }

    @Test
    fun isAppVersionTextDisplayed() {
        showDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.application_version_title), R.id.appVersionTitle)
    }

    @Test
    fun isFbFanPageDeepLinkDisplayedAndActive() {
        showDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.toast_facebook_fanpage), R.id.fanpageLinkText)
        HomeRobot().performClickOnElementWithId(R.id.fanpageLinkText)
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack()
        isDialogClosed()
    }


}