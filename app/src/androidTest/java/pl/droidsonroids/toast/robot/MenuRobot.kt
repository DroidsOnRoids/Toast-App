package pl.droidsonroids.toast.robot

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers.not
import pl.droidsonroids.toast.function.matchers.isBottomNavigationItemViewChecked
import pl.droidsonroids.toast.function.matchers.isBottomViewElementWithCorrectTitle

class MenuRobot : BaseRobot() {
    fun checkIfBottomNavigationItemViewIsChecked(id: Int): MenuRobot {
        onView(withId(id))
                .check(matches(isBottomNavigationItemViewChecked()))
        return this
    }

    fun checkIfBottomNavigationItemViewIsNotChecked(id: Int): MenuRobot {
        onView(withId(id))
                .check(matches(not(isBottomNavigationItemViewChecked())))
        return this
    }

    fun checkIfBottomNavigationItemViewHasCorrectTitle(title: String, id: Int): MenuRobot {
        onView(withId(id))
                .check(matches(isBottomViewElementWithCorrectTitle(title)))
        return this
    }
}