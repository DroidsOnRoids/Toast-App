package pl.droidsonroids.toast.utils

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ERROR_TEXT = "error_text"

@RunWith(MockitoJUnitRunner::class)
class StringResourceProviderTest {

    @Mock private lateinit var context: Context
    private lateinit var stringResourceProvider: StringResourceProvider


    @Before
    fun setUp() {
        stringResourceProvider = StringResourceProvider(context)

        whenever(context.getString(any())).thenReturn(ERROR_TEXT)
    }

    @Test
    fun getString() {
        val error = stringResourceProvider.getString(0)
        assert(error == ERROR_TEXT)
    }

}