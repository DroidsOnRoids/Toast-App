package pl.droidsonroids.toast.app.utils.callbacks

import android.databinding.Observable

class OnPropertyChangedSkippableCallback(private val action: () -> Unit) : Observable.OnPropertyChangedCallback() {

    var skipNextInvocation = false

    override fun onPropertyChanged(observable: Observable?, propertyId: Int) {
        if (skipNextInvocation) {
            skipNextInvocation = false
        } else {
            action()
        }
    }

}