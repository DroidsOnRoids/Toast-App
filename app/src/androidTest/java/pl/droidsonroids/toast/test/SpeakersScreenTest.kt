package pl.droidsonroids.toast.test

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.function.getString
import pl.droidsonroids.toast.robot.SpeakersRobot

class SpeakersScreenTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    private fun goToSpeakersScreen() {
        with(SpeakersRobot()) {
            performClickOnElementWithId(R.id.actionSpeakers)
        }
    }

    private fun goToSearchScreen() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            performClickOnElementWithId(R.id.searchImageButton)
        }
    }

    @Test
    fun isToolbarDisplayed() {
        val toolbarTitle = getString(R.string.speakers_title)
        goToSpeakersScreen()
        with(SpeakersRobot()){
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }

    @Test
    fun isSearchIconDisplayed() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.searchImageButton)
        }
    }

    @Test
    fun isSpeakerSelectedOnSpeakersScreen() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
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

    @Test
    fun isSearchIconDisplayed() {
        goToSpeakersScreen()
        with(SpeakersRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.searchImageButton)
        }
    }

    @Test
    fun isEveryElementOnSearchScreenDisplayed() {
        goToSearchScreen()
        with(SpeakersRobot()) {
            checkIfElementWithIdIsDisplayed(R.id.searchBox)
            checkIfHomeButtonIsDisplayed()
            checkIfHintIsDisplayed(R.id.searchBox, getString(R.string.search_hint))
        }
    }

    @Test
    fun isSpeakersScreenDisplayedAfterClickingOnBackButton() {
        with(SpeakersRobot()) {
            goToSearchScreen()
            performNavigateUp()
            checkIfElementWithIdIsDisplayed(R.id.searchImageButton)
        }
    }
}
