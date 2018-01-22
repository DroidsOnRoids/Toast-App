package pl.droidsonroids.toast.app.utils

import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonroids.toast.utils.StringProvider

private const val ERROR_TEXT = "error_text"

@RunWith(MockitoJUnitRunner::class)
class ContactFormValidatorTest {

    private val VALID_EMAILS = arrayOf("email@yahoo.com", "email-100@yahoo.com", "Email.100@yahoo.com", "email111@email.com", "email-100@email.net", "email.100@email.com.au", "emAil@1.com", "email@gmail.com.com", "email+100@gmail.com", "emAil-100@yahoo-test.com", "email_100@yahoo-test.ABC.CoM")
    private val INVALID_EMAILS = arrayOf("あいうえお@example.com", "email@111", "email", "email@.com.my", "email123@gmail.", "email123@.com", "email123@.com.com", ".email@email.com", "email()*@gmAil.com", "eEmail()*@gmail.com", "email@%*.com", "email..2002@gmail.com", "email.@gmail.com", "email@email@gmail.com", "email@gmail.com.")
    private val VALID_NAMES = arrayOf("name", "NAME", "Name", "Name Surname", "Name-Name2")
    private val INVALID_NAMES = arrayOf("name.  ", " ", "n", "Name@Name", "Name+Surname", "_Name_", "Name.Name")
    private val VALID_MESSAGES = arrayOf("it must contain 40 characters, it must c", "it must contain 250 characters it must characters it must contain 250 characters it must characters it must contain 250 characters it must characters it must contain 250 characters it must characters it must contain 250 characters it must characters!")
    private val INVALID_MESSAGES = arrayOf("", "it contains less than 40 char", "it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters!")

    @Mock
    lateinit var stringProvider: StringProvider

    @InjectMocks
    lateinit var contactFormValidator: ContactFormValidator

    @Before
    fun setUp() {
        whenever(stringProvider.getString(anyInt())).thenReturn(ERROR_TEXT)
    }

    @Test
    fun emailValidationShouldBeValid() {
        VALID_EMAILS.forEach {
            val emailError = contactFormValidator.getEmailError(it)
            assertThat(emailError, `is`(nullValue()))
        }
    }

    @Test
    fun emailValidationShouldBeNotValid() {
        INVALID_EMAILS.forEach {
            val emailError = contactFormValidator.getEmailError(it)
            assertThat(emailError, equalTo(ERROR_TEXT))
        }
    }

    @Test
    fun nameValidationShouldBeValid() {
        VALID_NAMES.forEach {
            val nameError = contactFormValidator.getNameError(it)
            assertThat(nameError, `is`(nullValue()))
        }
    }

    @Test
    fun nameValidationShouldBeNotValid() {
        INVALID_NAMES.forEach {
            val nameError = contactFormValidator.getNameError(it)
            assertThat(nameError, equalTo(ERROR_TEXT))
        }
    }

    @Test
    fun messageValidationShouldBeValid() {
        VALID_MESSAGES.forEach {
            val messageError = contactFormValidator.getMessageError(it)
            assertThat(messageError, `is`(nullValue()))
        }
    }

    @Test
    fun messageValidationShouldBeNotValid() {
        INVALID_MESSAGES.forEach {
            val messageError = contactFormValidator.getMessageError(it)
            assertThat(messageError, equalTo(ERROR_TEXT))
        }
    }
}