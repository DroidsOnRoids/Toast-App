package pl.droidsonroids.toast.app.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.notifications.FcmSubscriptionManager
import pl.droidsonroids.toast.utils.consume
import javax.inject.Inject

class SettingsActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    @Inject
    lateinit var fcmSubscriptionManager: FcmSubscriptionManager
    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFragmentIfNeeded(shouldAdd = savedInstanceState == null)
        setupActionBar()
    }

    private fun addFragmentIfNeeded(shouldAdd: Boolean) {
        if (shouldAdd) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, NotificationPreferenceFragment())
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPrefs.registerOnSharedPreferenceChangeListener(fcmSubscriptionManager)
    }

    override fun onPause() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(fcmSubscriptionManager)
        super.onPause()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> consume { onBackPressed() }
                else -> super.onOptionsItemSelected(item)
            }

    class NotificationPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pref_notification)
        }
    }
}
