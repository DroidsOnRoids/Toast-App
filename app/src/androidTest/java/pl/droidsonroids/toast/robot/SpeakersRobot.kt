package pl.droidsonroids.toast.robot

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import pl.droidsonroids.toast.R

class SpeakersRobot : BaseRobot() {

    fun checkIfTextIsTyped(text: String, id: Int): SpeakersRobot {
        onView(withId(id))
                .perform(typeText(text))
        return this
    }

    fun checkSearchButtonFunctionality(): SpeakersRobot {
        onView(withId(R.id.searchBox))
                .perform(pressImeActionButton())
        return this
    }
}