package pl.droidsonroids.toast.app.speakers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_speakers_search.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity

class SpeakersSearchActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context): Intent = Intent(context, SpeakersSearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speakers_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}