package pl.droidsonroids.toast.robot

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers.allOf
import pl.droidsonroids.toast.app.base.StateViewHolder

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
                .check(matches(isEnabled()))
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

    fun performClickOnRecyclerViewElement(id: Int, position: Int): BaseRobot {
        onView(withId(id))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
        return this
    }

    fun performTyping(text: String, id: Int): BaseRobot {
        onView(withId(id))
                .perform(ViewActions.typeText(text))
        return this
    }
}