package pl.droidsonroids.toast.test

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.robot.SpeakersRobot

class TestSpeakersScreen {
    @Suppress("unused")
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    private fun goToSpeakersScreen() {
        SpeakersRobot().performClickOnElementWithId(R.id.actionSpeakers)
    }

    private fun goToSearchScreen() {
        goToSpeakersScreen()
        SpeakersRobot().performClickOnElementWithId(R.id.searchImageButton)
    }

    @Test
    fun isSpeakerSelectedOnSpeakersScreen() {
        goToSpeakersScreen()
        SpeakersRobot().performClickOnRecyclerViewElement(R.id.speakersRecyclerView, 0)
    }

    @Test
    fun isSpeakerSelectedOnSearchScreen() {
        goToSearchScreen()
        SpeakersRobot().performTyping("a", R.id.searchBox)
        SpeakersRobot().checkIfSearchIsPerformed()
                .performClickOnRecyclerViewElement(R.id.speakersSearchRecyclerView, 0)
    }
}
