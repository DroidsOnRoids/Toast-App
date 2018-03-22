package pl.droidsonroids.toast.app.utils

import android.support.design.widget.Snackbar
import android.view.View
import pl.droidsonroids.toast.app.utils.extensions.showSnackbar
import pl.droidsonroids.toast.utils.NavigationRequest
import java.util.*
import javax.inject.Inject

class SnackbarQueue @Inject constructor() {
    private var view: View? = null
    private var options: Snackbar.() -> Unit = {}
    private val queue = LinkedList<NavigationRequest.SnackBar>()

    fun attach(view: View, options: Snackbar.() -> Unit = {}) {
        this.view = view
        this.options = options
        if (queue.isNotEmpty()) {
            showNextSnackbar()
        }
    }

    fun postSnackbarRequest(request: NavigationRequest.SnackBar) {
        if (!queue.contains(request)) {
            queue.add(request)
            if (queue.size == 1) {
                showNextSnackbar()
            }
        }
    }

    private fun showNextSnackbar() {
        val snackbarRequest = queue.peek()
        val view = view
        if (snackbarRequest != null && view != null) {
            view.showSnackbar(snackbarRequest, apply = options, onDismiss = {
                queue.poll()
                showNextSnackbar()
            })
        }
    }

    fun detach() {
        view = null
        options = {}
    }
}