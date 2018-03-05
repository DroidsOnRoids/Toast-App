package pl.droidsonroids.toast.test

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.function.getString
import pl.droidsonroids.toast.robot.MenuRobot


class TestBottomsMenu {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun isEveryTabInBottomMenuDisplayed() {
        with(MenuRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.actionEvents)
            checkIfElementWithIdIsDisplayed(R.id.actionSpeakers)
            checkIfElementWithIdIsDisplayed(R.id.actionContact)
        }
    }

    @Test
    fun isEveryTabInBottomMenuWithCorrectTitle() {
        with(MenuRobot()) {
            checkIfBottomNavigationItemViewHasCorrectTitle(getString(R.string.events_title), R.id.actionEvents)
            checkIfBottomNavigationItemViewHasCorrectTitle(getString(R.string.speakers_title), R.id.actionSpeakers)
            checkIfBottomNavigationItemViewHasCorrectTitle(getString(R.string.contact_title), R.id.actionContact)
        }
    }

    @Test
    fun isEveryTabInBottomMenuClickable() {
        with(MenuRobot()) {
            checkIfElementWithIdIsClickable(R.id.actionEvents)
            checkIfElementWithIdIsClickable(R.id.actionSpeakers)
            checkIfElementWithIdIsClickable(R.id.actionContact)
        }
    }

    @Test
    fun isBottomMenuInDefaultState() {
        with(MenuRobot()) {
            checkIfBottomNavigationItemViewIsChecked(R.id.actionEvents)
        }
    }

    @Test
    fun isOnlyOneTabActiveAtTheSameTime() {
        with(MenuRobot()) {
            performClickOnElementWithId(R.id.actionSpeakers)
            checkIfBottomNavigationItemViewIsChecked(R.id.actionSpeakers)
            checkIfBottomNavigationItemViewIsNotChecked(R.id.actionEvents)
            checkIfBottomNavigationItemViewIsNotChecked(R.id.actionContact)
        }
    }

    @Test
    fun isSpeakersViewDisplayed() {
        val toolbarTitle = getString(R.string.speakers_title)
        with(MenuRobot()) {
            performClickOnElementWithId(R.id.actionSpeakers)
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }

    @Test
    fun isEventsViewDisplayed() {
        val toolbarTitle = getString(R.string.events_title)
        with(MenuRobot()) {
            performClickOnElementWithId(R.id.actionEvents)
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }

    @Test
    fun isContactViewDisplayed() {
        val toolbarTitle = getString(R.string.contact_title)
        with(MenuRobot()) {
            performClickOnElementWithId(R.id.actionContact)
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }
}