package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import pl.droidsonrioids.toast.data.api.EventsManager
import javax.inject.Inject

class DummyViewModel @Inject constructor(private val eventsManager: EventsManager) : ViewModel() {
    val text = "Under Construction"
}