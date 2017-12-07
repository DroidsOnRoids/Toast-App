package pl.droidsonrioids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import pl.droidsonrioids.toast.utils.LoadingStatus


abstract class LoadingViewModel : ViewModel() {
    val loadingStatus: ObservableField<LoadingStatus> = ObservableField()

    abstract fun retryLoading()
}