@file:JvmName("ContextExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


fun Context.copyTextToClipboard(email: String, label: String) {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.primaryClip = ClipData.newPlainText(label, email)
}