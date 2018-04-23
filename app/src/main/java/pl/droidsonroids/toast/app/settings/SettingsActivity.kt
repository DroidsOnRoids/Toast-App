package pl.droidsonroids.toast.app.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.notifications.NotificationManager
import pl.droidsonroids.toast.utils.consume
import javax.inject.Inject

class SettingsActivity : BaseActivity() {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, NotificationPreferenceFragment())
                    .commit()
        }
        setupActionBar()
    }

    override fun onResume() {
        super.onResume()
        sharedPrefs.registerOnSharedPreferenceChangeListener(notificationManager)
    }

    override fun onPause() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(notificationManager)
        super.onPause()
    }

    //    override fun isMultiPane() = false

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
