package pl.droidsonrioids.toast.viewmodels

import android.databinding.ObservableField
import pl.droidsonrioids.toast.utils.LoadingStatus


interface LoadingViewModel {
    val loadingStatus: ObservableField<LoadingStatus>

    fun retryLoading()
}