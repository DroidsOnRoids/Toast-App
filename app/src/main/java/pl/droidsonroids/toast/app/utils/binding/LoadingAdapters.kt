package pl.droidsonroids.toast.app.utils.binding

import android.databinding.BindingAdapter
import android.view.View
import pl.droidsonroids.toast.app.utils.extensions.hideWithFading
import pl.droidsonroids.toast.app.utils.extensions.showWithFading
import pl.droidsonroids.toast.utils.LoadingStatus

@BindingAdapter("loadingContainerVisibility", "fadingEnabled")
fun View.setLoadingContainerVisibility(loadingStatus: LoadingStatus, fadingEnabled: Boolean) {
    if (fadingEnabled) {
        fadeLoadingContainer(loadingStatus)
    } else {
        setVisible(loadingStatus != LoadingStatus.SUCCESS)
    }
}

@BindingAdapter("loadingIndicatorVisibility")
fun View.setLoadingIndicatorVisibility(loadingStatus: LoadingStatus) {
    visibility = when (loadingStatus) {
        LoadingStatus.PENDING -> View.VISIBLE
        else -> View.INVISIBLE
    }
}

@BindingAdapter("connectionErrorContainerVisibility", "fadingEnabled")
fun View.setConnectionErrorContainerVisibility(loadingStatus: LoadingStatus, fadingEnabled: Boolean) {
    if (fadingEnabled) {
        fadeErrorContainer(loadingStatus)
    } else {
        setVisible(loadingStatus == LoadingStatus.ERROR)
    }
}

private fun View.fadeErrorContainer(loadingStatus: LoadingStatus) {
    when (loadingStatus) {
        LoadingStatus.ERROR -> showWithFading()
        else -> hideWithFading()
    }
}

private fun View.fadeLoadingContainer(loadingStatus: LoadingStatus) {
    when (loadingStatus) {
        LoadingStatus.SUCCESS -> hideWithFading()
        else -> showWithFading()
    }
}