package pl.droidsonrioids.toast

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

open class BaseRobot {

    fun checkIsToolbarTitle(title: String): BaseRobot {
        onView(withToolbarTitle(title))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkIfElementWithIdIsDisplayed(id: Int): BaseRobot {
        onView(withId(id))
                .check(matches(isDisplayed()))
        return this
    }
//Custom matchers

fun withToolbarTitle(title: CharSequence): Matcher<View> = object : TypeSafeMatcher<View>() {
    override fun matchesSafely(item: View) = item is TextView && item.text == title

    override fun describeTo(description: Description) {
        description.appendText("with title: $title")
    }
}
}