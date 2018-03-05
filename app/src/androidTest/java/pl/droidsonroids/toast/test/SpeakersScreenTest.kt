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

    @Test
    fun isToolbarDisplayed() {
        val toolbarTitle = getString(R.string.speakers_title)
        with(SpeakersRobot()) {
            goToSpeakersScreen()
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }

    @Test
    fun isSearchIconDisplayed() {
        with(SpeakersRobot()) {
            goToSpeakersScreen()
            checkIfElementWithIdIsDisplayed(R.id.searchImageButton)
        }
    }

    @Test
    fun isSpeakerSelectedOnSpeakersScreen() {
        with(SpeakersRobot()) {
            goToSpeakersScreen()
            performClickOnRecyclerViewElement(R.id.speakersRecyclerView, 0)
        }
    }

    @Test
    fun isSpeakerSelectedOnSearchScreen() {
        with(SpeakersRobot()) {
            goToSearchScreen()
            performTyping("a", R.id.searchBox)
            checkIfSearchIsPerformed()
            performClickOnRecyclerViewElement(R.id.speakersSearchRecyclerView, 0)
        }
    }

    @Test
    fun isEveryElementOnSearchScreenDisplayed() {
        with(SpeakersRobot()) {
            goToSearchScreen()
            checkIfElementWithIdIsDisplayed(R.id.searchBox)
            checkIfHomeButtonIsDisplayed()
            checkIfHintOnEditTextIsCorrect(R.id.searchBox, getString(R.string.search_hint))
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

    @Test
    fun isSortingBarDisplayed() {
        with(SpeakersRobot()) {
            goToSpeakersScreen()
            checkIfElementWithIdIsDisplayed(R.id.sortingBarLayout)
            checkIfElementWithIdIsDisplayed(R.id.arrowDownImage)
        }
    }

    @Test
    fun isSortingBarExpanded() {
        with(SpeakersRobot()) {
            goToSpeakersScreen()
            performClickOnElementWithId(R.id.titleSortingLayout)
            checkIfElementWithIdIsDisplayed(R.id.arrowDownImage)
            checkIfElementWithIdIsDisplayed(R.id.alphabeticalDivider)
            checkIfElementWithIdIsDisplayed(R.id.alphabeticalSortImage)
            checkIfTextIsCorrect(getString(R.string.alphabetical), R.id.alphabeticalText)
            checkIfElementWithIdIsDisplayed(R.id.dateDivider)
            checkIfElementWithIdIsDisplayed(R.id.dateSortImage)
            checkIfTextIsCorrect(getString(R.string.date), R.id.dateText)
        }
    }
}
