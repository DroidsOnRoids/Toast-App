package pl.droidsonroids.toast.robot

import android.content.Intent
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.matcher.IntentMatchers.hasData
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.function.getString

class InfoDialogRobot : BaseRobot() {

    fun checkIfIntentOpensFacebook() {
        intended(allOf(
                hasAction(equalTo(Intent.ACTION_VIEW)),
                hasData(Uri.parse(getString(R.string.toast_fanpage_url)))))
    }

    fun showDialog() {
        with(InfoDialogRobot()) {
            openMenuOverflow()
            performClickOnElementWithText(getString(R.string.about_app))
        }
    }

    fun openMenuOverflow() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    fun isDialogClosed() {
        with(InfoDialogRobot()) {
            checkIfElementWithIdIsNotPresentInHierarchy(R.id.toastLogoImage)
        }
    }

}