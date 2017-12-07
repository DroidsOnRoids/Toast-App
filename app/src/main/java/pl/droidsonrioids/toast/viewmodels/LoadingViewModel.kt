package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import pl.droidsonrioids.toast.utils.LoadingStatus


abstract class LoadingViewModel : ViewModel() {
    var loadingStatus: ObservableField<LoadingStatus> = ObservableField()
        private set

    abstract fun retryLoading()
}