package pl.droidsonroids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import javax.inject.Inject


class SpeakersViewModel @Inject constructor() : ViewModel() {
    val isSortingDetailsVisible: ObservableField<Boolean> = ObservableField()

    init {
        isSortingDetailsVisible.set(false)
    }

    fun toggleSortingDetailsVisibility() {
        isSortingDetailsVisible.set(!isSortingDetailsVisible.get())
    }
}