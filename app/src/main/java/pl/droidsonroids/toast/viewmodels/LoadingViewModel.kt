package pl.droidsonroids.toast.viewmodels

import android.databinding.ObservableField
import pl.droidsonroids.toast.utils.LoadingStatus


interface LoadingViewModel {
    val loadingStatus: ObservableField<LoadingStatus>

    fun retryLoading()

    val isFadingEnabled get() = false
}