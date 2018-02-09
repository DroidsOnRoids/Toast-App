package pl.droidsonroids.toast.app.utils.binding

import android.databinding.BindingAdapter
import android.view.View
import pl.droidsonroids.toast.utils.LoadingStatus

@BindingAdapter("loadingContainerVisibility", "fadingEnabled")
fun View.setLoadingContainerVisibility(loadingStatus: LoadingStatus, fadingEnabled: Boolean) {
    if (fadingEnabled) {
        fadeLoadingContainer(loadingStatus, LoadingStatus.PENDING)
    } else {
        setVisibility(loadingStatus == LoadingStatus.PENDING)
    }
}

private fun View.fadeLoadingContainer(loadingStatus: LoadingStatus, visibleStatus: LoadingStatus) {
    when (loadingStatus) {
        visibleStatus -> {
            animate().alpha(1f).withStartAction {
                visibility = View.VISIBLE
            }.start()
        }
        else -> {
            animate().alpha(0f).withEndAction {
                visibility = View.GONE
            }.start()
        }
    }
}


@BindingAdapter("loadingIndicatorVisibility")
fun setLoadingIndicatorVisibility(loadingContainer: View, loadingStatus: LoadingStatus) {
    loadingContainer.visibility = when (loadingStatus) {
        LoadingStatus.PENDING -> View.VISIBLE
        else -> View.INVISIBLE
    }
}

@BindingAdapter("connectionErrorContainerVisibility", "fadingEnabled")
fun View.setConnectionErrorContainerVisibility(loadingStatus: LoadingStatus, fadingEnabled: Boolean) {
    if (fadingEnabled) {
        fadeLoadingContainer(loadingStatus, LoadingStatus.ERROR)
    } else {
        setVisibility(loadingStatus == LoadingStatus.ERROR)
    }
}