package pl.droidsonrioids.toast

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_info_dialog.*

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
    }

    private fun setDialogBackgroundDrawable() {
        dialog.window.setBackgroundDrawableResource(R.drawable.rectangle_with_round_corners)
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
            intent.data = Uri.parse(TOAST_FANPAGE_URL)
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            showBrowserNotFoundErrorToast()
        }
    }

    private fun showBrowserNotFoundErrorToast() {
        Toast.makeText(activity, getString(R.string.error_internet_browser_not_found), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TOAST_FANPAGE_URL = "https://www.facebook.com/toastwroclaw/"
    }
}

