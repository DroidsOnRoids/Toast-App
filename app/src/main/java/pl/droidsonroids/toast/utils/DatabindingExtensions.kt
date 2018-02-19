package pl.droidsonroids.toast.utils

import android.databinding.Observable
import android.databinding.ObservableField

fun <T> ObservableField<T>.addOnPropertyChangedCallback(onChange: () -> Unit): Observable.OnPropertyChangedCallback {
    return object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            onChange()
        }
    }.also { addOnPropertyChangedCallback(it) }
}