package pl.droidsonroids.toast.robot

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf

abstract class BaseRobot {

    fun checkIfToolbarWithTitleIsDisplayed(title: String, toolbarId: Int): BaseRobot {
        onView(allOf(withText(title), isDescendantOfA(withId(toolbarId))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkIfElementWithIdIsNotPresentInHierarchy(id: Int): BaseRobot {
        onView(withId(id))
                .check(doesNotExist())
        return this
    }

    fun checkIfElementWithIdIsDisplayed(id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkIfElementWithIdIsDisplayedInDialog(id: Int): BaseRobot {
        onView(withId(id))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        return this
    }

    fun checkIfElementWithIdIsClickable(id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(isClickable()))
        return this
    }

    fun checkIfTextIsCorrect(text: String, id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(withText(text)))
        return this

    }

    fun performClickOnElementWithId(id: Int): BaseRobot {
        onView(withId(id))
                .perform(click())
        return this
    }
}