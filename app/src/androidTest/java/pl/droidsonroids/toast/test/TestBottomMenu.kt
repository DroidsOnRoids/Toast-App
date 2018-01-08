package pl.droidsonroids.toast.test

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.robot.MenuRobot

class TestBottomMenu {
    @Suppress("unused")
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun isEveryTabDisplayedInBottomMenu() {
        MenuRobot().checkIfElementWithIdIsDisplayed(R.id.actionEvents)
                .checkIfTextIsCorrect("Events", R.id.actionEvents)
                .checkIfElementWithIdIsDisplayed(R.id.actionSpeakers)
                .checkIfTextIsCorrect("Speakers", R.id.actionSpeakers)
                .checkIfElementWithIdIsDisplayed(R.id.actionContact)
                .checkIfTextIsCorrect("Contact", R.id.actionContact)
    }

}