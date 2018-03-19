package pl.droidsonroids.toast.test

import android.support.test.espresso.Espresso
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.robot.SpeakersRobot

class SpeakerDetailsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun isHeaderDisplayedWithProperElements() {
        with(SpeakersRobot()) {
            goToSpeakerDetailsScreen()
            checkIfElementWithIdIsDisplayed(R.id.collapsingToolbar)
            checkIfElementWithIdIsDisplayed(R.id.avatarImage)
            checkIfElementWithIdIsDisplayed(R.id.avatarBorderSmall)
            checkIfElementWithIdIsDisplayed(R.id.avatarBorderBig)
            checkIfElementWithIdIsDisplayed(R.id.speakerName)
            checkIfElementWithIdIsDisplayed(R.id.speakerJob)
        }
    }

    @Test
    fun isSpeakersListDisplayedAfterClickingBackFromSpeakerDetails() {
        with(SpeakersRobot()) {
            goToSpeakerDetailsScreen()
            Espresso.pressBack()
            checkIfElementWithIdIsDisplayed(R.id.searchImageButton)
        }
    }
}
