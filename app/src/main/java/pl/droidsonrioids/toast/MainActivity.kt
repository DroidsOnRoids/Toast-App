package pl.droidsonrioids.toast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

const val INFO_DIALOG_TAG = "info_dialog_tag"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.about_item -> consume { showInfoDialog() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun showInfoDialog() {
        InfoDialogFragment().show(supportFragmentManager, INFO_DIALOG_TAG)
    }

    private fun consume(func: () -> Unit): Boolean {
        func()
        return true
    }
}
