package pl.droidsonroids.toast.robot

import android.content.Intent
import android.net.Uri
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.matcher.IntentMatchers.hasData
import android.support.test.espresso.intent.matcher.UriMatchers.hasHost
import android.support.test.espresso.intent.matcher.UriMatchers.hasPath
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.function.getString

class InfoDialogRobot : BaseRobot() {
    fun checkIfIntentedIntentOpensFacebook() {
        intended(allOf(
                hasAction(equalTo(Intent.ACTION_VIEW)),
                hasData(Uri.parse(getString(R.string.toast_fanpage_url)))))
    }
}