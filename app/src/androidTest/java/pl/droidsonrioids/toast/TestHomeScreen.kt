package pl.droidsonrioids.toast

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class TestHomeScreen {
    @Suppress("unused")
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun isToolbarDisplayed() {
        val toolbarTitle = getString(R.string.toolbar_events_title)
        HomeRobot().checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)

    }

    @Test
    fun isMenuOverflowDisplayed(){
        HomeRobot().checkIfElementWithIdIsDisplayed(R.id.about_item)
    }

}
