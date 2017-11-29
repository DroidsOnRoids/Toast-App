package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import pl.droidsonrioids.toast.data.api.ApiManager
import javax.inject.Inject

class DummyViewModel @Inject constructor(private val apiManager: ApiManager) : ViewModel() {
    val text = "Under Construction"
}