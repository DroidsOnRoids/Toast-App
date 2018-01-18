package pl.droidsonroids.toast.test

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.robot.SpeakersRobot

class TestSpeakersScreen {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    private fun goToSpeakersScreen() {
        with(SpeakersRobot()){
            performClickOnElementWithId(R.id.actionSpeakers)
        }
    }

    private fun goToSearchScreen() {
        goToSpeakersScreen()
        with(SpeakersRobot()){
            performClickOnElementWithId(R.id.searchImageButton)
        }
    }

    @Test
    fun isSpeakerSelectedOnSpeakersScreen() {
        goToSpeakersScreen()
        with(SpeakersRobot()){
            performClickOnRecyclerViewElement(R.id.speakersRecyclerView, 0)
        }
    }

    @Test
    fun isSpeakerSelectedOnSearchScreen() {
        goToSearchScreen()
        with(SpeakersRobot()) {
            performTyping("a", R.id.searchBox)
            checkIfSearchIsPerformed()
            performClickOnRecyclerViewElement(R.id.speakersSearchRecyclerView, 0)
        }
    }
}
