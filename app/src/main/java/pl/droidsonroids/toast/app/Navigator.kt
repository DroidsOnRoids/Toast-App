package pl.droidsonroids.toast.app

import android.content.Context
import pl.droidsonroids.toast.utils.NavigationRequest

interface Navigator {
    fun dispatch(context: Context, navigationRequest: NavigationRequest)
}