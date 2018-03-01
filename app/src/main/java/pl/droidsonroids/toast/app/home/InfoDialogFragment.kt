package pl.droidsonroids.toast.app.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_info_dialog.*
import pl.droidsonroids.toast.BuildConfig
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.utils.extensions.showSnackbar
import pl.droidsonroids.toast.utils.NavigationRequest

class InfoDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_info_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSetUp()
    }

    private fun initialSetUp() {
        setDialogBackgroundDrawable()
        setButtonsOnClickListeners()
        setVersionText()
    }

    private fun setDialogBackgroundDrawable() {
        dialog.window.setBackgroundDrawableResource(R.drawable.dialog_background_round_rectangle)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
    }

    private fun setButtonsOnClickListeners() {
        setCloseImageButtonOnClickListener()
        setFanpageLinkTextOnClickListener()
    }

    private fun setCloseImageButtonOnClickListener() {
        closeImageButton.setOnClickListener { dismiss() }
    }

    private fun setFanpageLinkTextOnClickListener() {
        fanpageLinkText.setOnClickListener {
            openFanpageSite()
            dismiss()
        }
    }

    private fun openFanpageSite() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.toast_fanpage_url))
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            showBrowserNotFoundErrorToast()
        }
    }

    private fun showBrowserNotFoundErrorToast() {
        (activity as MainActivity).mainCoordinatorLayout.showSnackbar(NavigationRequest.SnackBar(R.string.error_internet_browser_not_found))
    }

    private fun setVersionText() {
        appVersionText.text = getString(R.string.application_version, BuildConfig.VERSION_NAME)
    }
}

