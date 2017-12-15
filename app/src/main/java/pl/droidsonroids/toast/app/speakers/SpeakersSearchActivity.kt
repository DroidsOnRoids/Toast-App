package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.content.Intent
import pl.droidsonroids.toast.app.base.BaseActivity

class SpeakersSearchActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context): Intent = Intent(context, SpeakersSearchActivity::class.java)
    }
}