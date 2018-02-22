package pl.droidsonroids.toast.robot

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers.*
import pl.droidsonroids.toast.function.matchers.isHintOnEditTextCorrect
import pl.droidsonroids.toast.function.matchers.isHintOnTextInputLayoutCorrect

abstract class BaseRobot {

    private val homeButtonDescription = android.support.v7.appcompat.R.string.abc_action_bar_up_description

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

    fun checkIfElementWithIdIsNotDisplayed(id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(not(isDisplayed())))
        return this
    }

    fun checkIfElementWithTextIsDisplayed(text: String): BaseRobot {
        onView(withText(text))
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

    fun checkIfElementWithTextIsClickable(text: String): BaseRobot {
        onView(withText(text))
                .check(matches(isEnabled()))
        return this
    }


    fun checkIfTextIsCorrect(text: String, id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(withText(text)))
        return this
    }

    fun checkIfTextStartsWith(text: String, id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(withText(startsWith(text))))
        return this
    }

    fun checkIfSpinnerTextIsCorrect(text: String, id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(withSpinnerText(containsString(text))))
        return this
    }

    fun checkIfHomeButtonIsDisplayed(): BaseRobot {
        onView(withContentDescription(homeButtonDescription))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkIfHintOnTextInputLayoutIsCorrect(id: Int, text: String): BaseRobot {
        onView(withId(id))
                .check(matches(isHintOnTextInputLayoutCorrect(text)))
        return this
    }

    fun checkIfHintOnEditTextIsCorrect(id: Int, text: String): BaseRobot {
        onView(withId(id))
                .check(matches(isHintOnEditTextCorrect(text)))
        return this
    }

    fun performClickOnElementWithId(id: Int): BaseRobot {
        onView(withId(id))
                .perform(click())
        return this
    }

    fun performClickOnElementWithText(string: String): BaseRobot {
        onView(withText(string))
                .perform(click())
        return this
    }

    fun performClickOnDataWithText(string: String): BaseRobot {
        onData(allOf(`is`(string)))
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

    fun performNavigateUp(): BaseRobot {
        onView(withContentDescription(homeButtonDescription))
                .perform(click())
        return this
    }
}