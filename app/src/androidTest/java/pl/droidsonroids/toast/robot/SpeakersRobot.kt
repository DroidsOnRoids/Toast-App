package pl.droidsonroids.toast.robot

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.matcher.ViewMatchers.withId
import pl.droidsonroids.toast.R

class SpeakersRobot : BaseRobot() {

    fun checkIfSearchIsPerformed(): SpeakersRobot {
        onView(withId(R.id.searchBox))
                .perform(pressImeActionButton())
        return this
    }

    fun goToSpeakersScreen() {
        with(SpeakersRobot()) {
            performClickOnElementWithId(R.id.actionSpeakers)
        }
    }

    fun goToSearchScreen() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            performClickOnElementWithId(R.id.searchImageButton)
        }
    }

    fun goToSpeakerDetailsScreen() {
        with(SpeakersRobot()) {
            goToSpeakersScreen()
            performClickOnRecyclerViewElement(R.id.speakersRecyclerView, 0)
        }
    }
}