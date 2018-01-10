package pl.droidsonroids.toast.viewmodels.contact

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import javax.inject.Inject

private const val I_WANT_TO = 0
private const val TALK = 1
private const val REWARD = 2
private const val PARTNER = 3

class ContactViewModel @Inject constructor() : ViewModel() {
    private val Any.simpleClassName: String get() = javaClass.simpleName

    val topic: ObservableField<Int> = ObservableField()
    val name: ObservableField<String> = ObservableField()
    val email: ObservableField<String> = ObservableField()
    val message: ObservableField<String> = ObservableField()

    fun onSendClick() {
        Log.d(simpleClassName, "Send message: ${message.get()} from ${name.get()} on topic: ${topic.get()}")
    }
}