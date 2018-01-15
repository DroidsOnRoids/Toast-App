package pl.droidsonroids.toast.app.utils

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.droidsonroids.toast.utils.StringResourceProvider

private const val ERROR_TEXT = "error_text"

@RunWith(MockitoJUnitRunner::class)
class ValidatorTest {

    private val VALID_EMAILS = arrayOf("email@yahoo.com", "email-100@yahoo.com", "Email.100@yahoo.com", "email111@email.com", "email-100@email.net", "email.100@email.com.au", "emAil@1.com", "email@gmail.com.com", "email+100@gmail.com", "emAil-100@yahoo-test.com", "email_100@yahoo-test.ABC.CoM")
    private val INVALID_EMAILS = arrayOf("あいうえお@example.com", "email@111", "email", "email@.com.my", "email123@gmail.", "email123@.com", "email123@.com.com", ".email@email.com", "email()*@gmAil.com", "eEmail()*@gmail.com", "email@%*.com", "email..2002@gmail.com", "email.@gmail.com", "email@email@gmail.com", "email@gmail.com.")
    private val VALID_NAMES = arrayOf("", "   ", "name", "NAME", "Name", "Name S2urname", "Name-Name2", "Name.Name", "2Name name name-name.name")
    private val INVALID_NAMES = arrayOf("name. ", "NAME ", " ", "n", "Name-Name2", "Name@Name", "Name+Surname", "_Name_")
    private val VALID_MESSAGES = arrayOf("it must contain 40 characters, it must c", "it must contain 250 characters it must characters it must contain 250 characters it must characters it must contain 250 characters it must characters it must contain 250 characters it must characters it must contain 250 characters it must characters!")
    private val INVALID_MESSAGES = arrayOf("", "it contains less than 40 characters", "it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters, it contains more than 250 characters!")

    private lateinit var stringResourceProvider: StringResourceProvider
    private lateinit var validator: Validator
    @Mock private lateinit var context: Context

    @Before
    fun setUp() {
        stringResourceProvider = StringResourceProvider(context)
        validator = Validator(stringResourceProvider)

        whenever(context.getString(any())).thenReturn(ERROR_TEXT)
    }

    @Test
    fun emailValidationShouldBeValid() {
        VALID_EMAILS.forEach {
            val emailError = validator.getEmailError(it)
            assert(emailError.isNullOrEmpty())
        }
    }

    @Test
    fun emailValidationShouldBeNotValid() {
        INVALID_EMAILS.forEach {
            val emailError = validator.getEmailError(it)
            assert(!emailError.isNullOrEmpty())
        }
    }

    @Test
    fun nameValidationShouldBeValid() {
        VALID_NAMES.forEach {
            val nameError = validator.getNameError(it)
            assert(nameError.isNullOrEmpty())
        }
    }

    @Test
    fun nameValidationShouldBeNotValid() {
        INVALID_NAMES.forEach {
            val nameError = validator.getNameError(it)
            assert(!nameError.isNullOrEmpty())
        }
    }

    @Test
    fun messageValidationShouldBeValid() {
        VALID_MESSAGES.forEach {
            val messageError = validator.getMessageError(it)
            assert(messageError.isNullOrEmpty())
        }
    }

    @Test
    fun messageValidationShouldBeNotValid() {
        INVALID_MESSAGES.forEach {
            val messageError = validator.getMessageError(it)
            assert(!messageError.isNullOrEmpty())
        }
    }
}