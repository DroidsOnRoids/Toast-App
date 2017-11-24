package pl.droidsonrioids.toast

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf

abstract class BaseRobot {

    fun checkIfToolbarWithTitleIsDisplayed(title: String, toolbarId: Int): BaseRobot {
        onView(allOf(withText(title), isDescendantOfA(withId(toolbarId))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkIfElementWithIdIsDisplayed(id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(isDisplayed()))
        return this
    }
}