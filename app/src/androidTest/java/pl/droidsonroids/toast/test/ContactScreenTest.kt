package pl.droidsonroids.toast.test

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.home.MainActivity
import pl.droidsonroids.toast.function.getString
import pl.droidsonroids.toast.robot.ContactRobot
import android.support.test.InstrumentationRegistry
import android.content.Context
import android.preference.PreferenceManager


class ContactScreenTest {

    @JvmField
    @Rule
    var activityRule: ActivityTestRule<MainActivity> = object : ActivityTestRule<MainActivity>(MainActivity::class.java, true, true) {
        override fun beforeActivityLaunched() {
            clearSharedPrefs(InstrumentationRegistry.getTargetContext())
            super.beforeActivityLaunched()
        }
    }

    private fun clearSharedPrefs(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.clear()
        editor.commit()
    }

    @Test
    fun isToolbarDisplayed() {
        val toolbarTitle = getString(R.string.contact_title)
        with(ContactRobot()) {
            goToContactScreen()
            checkIfToolbarWithTitleIsDisplayed(toolbarTitle, R.id.toolbar)
        }
    }

    @Test
    fun isEveryFieldInContactFormDisplayed() {
        with(ContactRobot()) {
            goToContactScreen()
            checkIfElementWithIdIsDisplayed(R.id.topicSpinner)
            checkIfElementWithIdIsDisplayed(R.id.contactNameInputLayout)
            checkIfHintOnTextInputLayoutIsCorrect(R.id.contactNameInputLayout, getString(R.string.your_name))
            checkIfElementWithIdIsDisplayed(R.id.contactEmailInputLayout)
            checkIfHintOnTextInputLayoutIsCorrect(R.id.contactEmailInputLayout, getString(R.string.email_address))
            checkIfElementWithIdIsDisplayed(R.id.contactMessageInputLayout)
            checkIfHintOnEditTextIsCorrect(R.id.contactMessageEditText, getString(R.string.your_message))
        }
    }

    @Test
    fun isSendButtonDisplayedInDefaultState() {
        with(ContactRobot()) {
            goToContactScreen()
            checkIfElementWithIdIsDisplayed(R.id.disabledSendButton)
            checkIfElementWithIdIsNotDisplayed(R.id.enabledSendButton)
        }
    }

    @Test
    fun isSpinnerWithTopicsExpandableAndHasCorrectData() {
        with(ContactRobot()) {
            goToContactScreen()
            performClickOnElementWithId(R.id.topicSpinner)
            performClickOnDataWithText(getString(R.string.speak_on_the_next_event))
            checkIfSpinnerTextIsCorrect(getString(R.string.speak_on_the_next_event), R.id.topicSpinner)
            performClickOnElementWithId(R.id.topicSpinner)
            performClickOnDataWithText(getString(R.string.claim_a_reward))
            checkIfSpinnerTextIsCorrect(getString(R.string.claim_a_reward), R.id.topicSpinner)
            performClickOnElementWithId(R.id.topicSpinner)
            performClickOnDataWithText(getString(R.string.i_want_to))
            performClickOnDataWithText(getString(R.string.become_a_partner))
            checkIfSpinnerTextIsCorrect(getString(R.string.become_a_partner), R.id.topicSpinner)
        }
    }

    private fun createCounterOutput(countedText: String): String {
        val countedTextCharNumber: Int = countedText.length
        val limitOfTypedText = " / 250"
        return countedTextCharNumber.toString() + limitOfTypedText
    }

    @Test
    fun isCounterVisibleAndWorkingCorrectly() {
        val countedText = "abcabcabc"
        with(ContactRobot()) {
            goToContactScreen()
            checkIfElementWithIdIsDisplayed(R.id.characterCounter)
            checkIfTextIsCorrect(createCounterOutput(""), R.id.characterCounter)
            scrollToAndPerformTyping(countedText, R.id.contactMessageEditText)
            checkIfTextIsCorrect(createCounterOutput(countedText), R.id.characterCounter)
        }
    }

    @Test
    fun isNameFieldValidated() {
        val name = "a"
        with(ContactRobot()) {
            goToContactScreen()
            scrollToAndPerformTyping(name, R.id.contactNameEditText)
            checkIfErrorTextIsCorrect(getString(R.string.invalid_name), R.id.contactNameInputLayout)
            performClearText(R.id.contactNameEditText)
            checkIfErrorTextIsCorrect(getString(R.string.empty_name_error), R.id.contactNameInputLayout)
            scrollToAndPerformTyping(name + name + name, R.id.contactNameEditText)
            checkIfErrorTextIsCorrect(null, R.id.contactNameInputLayout)
        }
    }

    @Test
    fun isEmailFieldValidated() {
        val email = "aa"
        with(ContactRobot()) {
            goToContactScreen()
            scrollToAndPerformTyping(email, R.id.contactEmailEditText)
            checkIfErrorTextIsCorrect(getString(R.string.invalid_email), R.id.contactEmailInputLayout)
            scrollToAndPerformTyping("@", R.id.contactEmailEditText)
            checkIfErrorTextIsCorrect(getString(R.string.invalid_email), R.id.contactEmailInputLayout)
            scrollToAndPerformTyping("oo", R.id.contactEmailEditText)
            checkIfErrorTextIsCorrect(getString(R.string.invalid_email), R.id.contactEmailInputLayout)
            scrollToAndPerformTyping(".p", R.id.contactEmailEditText)
            checkIfErrorTextIsCorrect(getString(R.string.invalid_email), R.id.contactEmailInputLayout)
            scrollToAndPerformTyping("l", R.id.contactEmailEditText)
            checkIfErrorTextIsCorrect(null, R.id.contactNameInputLayout)
            performClearText(R.id.contactEmailEditText)
            checkIfErrorTextIsCorrect(getString(R.string.empty_email_error), R.id.contactEmailInputLayout)
        }
    }

    @Test
    fun isMessageFieldValidated() {
        val message = "abcabcabcabcabcabcabcabcabcabcabcabcabc"
        with(ContactRobot()) {
            goToContactScreen()
            scrollToAndPerformTyping(message, R.id.contactMessageEditText)
            checkIfErrorTextIsCorrect(getString(R.string.min_message_length_error), R.id.contactMessageInputLayout)
            scrollToAndPerformTyping("a", R.id.contactMessageEditText)
            checkIfErrorTextIsCorrect(null, R.id.contactMessageInputLayout)
            scrollToAndPerformTyping(message + message + message + message + message + message, R.id.contactMessageEditText)
            checkIfErrorTextIsCorrect(getString(R.string.max_message_length_error), R.id.contactMessageInputLayout)
            performClearText(R.id.contactMessageEditText)
            checkIfErrorTextIsCorrect(getString(R.string.empty_message_error), R.id.contactMessageInputLayout)
        }

    }

    @Test
    fun isSendButtonDisabledAgainWhenMessageIsTooLong() {
        val message = "abcabcabcabcabcabcabcabcabcabcabcabcabcaabcabcabcaabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc"
        val name = "Julia"
        val email = "julia@julia.co"
        with(ContactRobot()) {
            goToContactScreen()
            performClickOnElementWithId(R.id.topicSpinner)
            performClickOnDataWithText(getString(R.string.speak_on_the_next_event))
            scrollToAndPerformTyping(name, R.id.contactNameEditText)
            scrollToAndPerformTyping(email, R.id.contactEmailEditText)
            scrollToAndPerformTyping(message, R.id.contactMessageEditText)
            checkIfSendButtonIsNotClickable()
        }
    }

    @Test
    fun isSendButtonInProperStateAndMessageSent() {
        val name = "Jack Sparrow"
        val email = "jack.sparrow@gmail.com"
        val message = "abcabcabcabcabcabcabcabcabcabcabcabcabcabc"
        with(ContactRobot()) {
            goToContactScreen()
            performClickOnElementWithId(R.id.topicSpinner)
            performClickOnDataWithText(getString(R.string.speak_on_the_next_event))
            checkIfSendButtonIsNotClickable()
            scrollToAndPerformTyping(name, R.id.contactNameEditText)
            checkIfSendButtonIsNotClickable()
            scrollToAndPerformTyping(email, R.id.contactEmailEditText)
            checkIfSendButtonIsNotClickable()
            scrollToAndPerformTyping(message, R.id.contactMessageEditText)
            checkIfElementWithIdIsClickable(R.id.enabledSendButton)
            scrollTo(R.id.enabledSendButton)
            performClickOnElementWithId(R.id.enabledSendButton)
            checkIfElementWithIdIsDisplayed(R.id.messageSent)
            checkIfElementWithIdIsDisplayed(R.id.messageSentIcon)
        }
    }
}