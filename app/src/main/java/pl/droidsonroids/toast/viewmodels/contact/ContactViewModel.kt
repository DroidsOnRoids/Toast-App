package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import javax.inject.Inject


class ContactViewModel @Inject constructor() : ViewModel() {
    val areInputsValid = ObservableField<Boolean>()
    val selectedTopicPosition = ObservableField<Int>()


    fun validateForm(areInputsValid: Boolean) {
        this.areInputsValid.set(areInputsValid)
    }

}