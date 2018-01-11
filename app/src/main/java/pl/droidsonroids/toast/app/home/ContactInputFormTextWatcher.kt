package pl.droidsonroids.toast.app.home

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import br.com.ilhasoft.support.validation.Validator


class ContactInputFormTextWatcher(private val view: View, private val validator: Validator) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        validator.toValidate()
    }

    override fun afterTextChanged(s: Editable) {

    }
}