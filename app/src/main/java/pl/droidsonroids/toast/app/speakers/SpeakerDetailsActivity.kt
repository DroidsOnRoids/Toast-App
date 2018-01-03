package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.content.Intent
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.utils.NavigationRequest

class SpeakerDetailsActivity : BaseActivity() {
    companion object {
        private const val SPEAKER_ID: String = "speaker_id"

        fun createIntent(context: Context, navigationRequest: NavigationRequest.SpeakerDetails): Intent {
            return Intent(context, SpeakerDetailsActivity::class.java)
                    .putExtra(SPEAKER_ID, navigationRequest.id)
        }
    }

}