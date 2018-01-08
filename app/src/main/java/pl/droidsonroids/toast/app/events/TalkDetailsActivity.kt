package pl.droidsonroids.toast.app.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.data.dto.event.TalkDto
import pl.droidsonroids.toast.utils.NavigationRequest

class TalkDetailsActivity : BaseActivity() {
    companion object {
        private const val TALK_DTO_KEY = "talkDto"
        fun createIntent(context: Context, navigationRequest: NavigationRequest.TalkDetails): Intent {
            return Intent(context, TalkDetailsActivity::class.java)
                    .putExtra(TALK_DTO_KEY, navigationRequest.talkDto)
        }
    }

    private val talkDto by lazy {
        intent.getParcelableExtra<TalkDto>(TALK_DTO_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_details)
    }
}