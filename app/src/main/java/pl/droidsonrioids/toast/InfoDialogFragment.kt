package pl.droidsonrioids.toast

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_info_dialog.*


class InfoDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_info_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonsOnClickListeners()
    }

    private fun setButtonsOnClickListeners() {
        setCloseImageButtonOnClickListener()
        setFanpageLinkTextOnClickListener()
    }

    private fun setCloseImageButtonOnClickListener() {
        closeImageButton.setOnClickListener {
            dismiss()
        }
    }

    private fun setFanpageLinkTextOnClickListener() {
        fanpageLinkText.setOnClickListener {
            openFanpageSite()
            dismiss()
        }
    }

    private fun openFanpageSite() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(TOAST_FANPAGE_URL)
        startActivity(intent)
    }

    companion object {
        const val TOAST_FANPAGE_URL = "https://www.facebook.com/toastwroclaw/"
    }
}

