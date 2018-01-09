package pl.droidsonroids.toast.app.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import br.com.ilhasoft.support.validation.Validator


class ContactInputFormTextWatcher(private val validator: Validator, private val view: View) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        validator.validate(view)
    }

    override fun afterTextChanged(s: Editable) {

    }
}