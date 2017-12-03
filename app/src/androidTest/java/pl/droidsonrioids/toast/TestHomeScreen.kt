package pl.droidsonrioids.toast

import android.support.test.espresso.Espresso.pressBack
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonrioids.toast.app.home.MainActivity
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice


class TestHomeScreen {
    @Suppress("unused")
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    private fun openDialog() {
        HomeRobot().performClickOnElementWithId(R.id.about_item)
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
        HomeRobot().checkIfElementWithIdIsDisplayed(R.id.about_item)
    }

    @Test
    fun isMenuOverflowClickable() {
        HomeRobot().checkIfElementWithIdIsClickable(R.id.about_item)
    }

    @Test
    fun isToastLogoDisplayed() {
        openDialog()
        HomeRobot().checkIfElementWithIdIsDisplayed(R.id.toastLogoImage)
    }

    @Test
    fun isHeartImageDisplayed() {
        openDialog()
        HomeRobot().checkIfElementWithIdIsDisplayed(R.id.hearthImageView)
    }

    /*
    Using workaround to check if Dialog is closed as Dialog does not have ID
    Test is checking if Toast Logo (visible on Dialog) is NOT displayed
     */

    @Test
    fun isDialogClosedAfterClickingOnCloseButton() {
        openDialog()
        HomeRobot().checkIfElementWithIdIsDisplayed(R.id.closeImageButton)
        HomeRobot().performClickOnElementWithId(R.id.closeImageButton)
        isDialogClosed()
    }

    @Test
    fun isDialogClosedAfterClickingOnBackButton() {
        openDialog()
        pressBack()
        isDialogClosed()
    }

    @Test
    fun isDialogNotDismissedAfterTappingOnDialog() {
        openDialog()
        HomeRobot().performClickOnElementWithId(R.id.toastLogoImage)
        HomeRobot().checkIfElementWithIdIsDisplayed(R.id.toastLogoImage)
    }

    @Test
    fun isDialogDismissedAfterTappingOutsideDialog() {
        openDialog()
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click(0, 300)
        isDialogClosed()
    }

    @Test
    fun isCreatedWithTextDisplayed() {
        openDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.created_with), R.id.createdWithText)
    }

    @Test
    fun isByToastTeamTextDisplayed() {
        openDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.by_toast_team), R.id.byToastTeamText)
    }

    @Test
    fun isMoreInfoTextDisplayed() {
        openDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.for_more_information_visit_our), R.id.moreInfoText)
    }

    @Test
    fun isAppVersionTextDisplayed() {
        openDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.application_version_title), R.id.appVersionTitle)
    }

    @Test
    fun isFbFanPageDeepLinkDisplayedAndActive() {
        openDialog()
        HomeRobot().checkIfTextIsCorrect(getString(R.string.toast_facebook_fanpage), R.id.fanpageLinkText)
        HomeRobot().performClickOnElementWithId(R.id.fanpageLinkText)
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack()
        isDialogClosed()
    }


}