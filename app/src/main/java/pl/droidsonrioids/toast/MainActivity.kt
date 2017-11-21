package pl.droidsonrioids.toast

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun showInfoDialog() {
        InfoDialogFragment().show(supportFragmentManager, INFO_DIALOG_TAG)
    }

    companion object {
        const val INFO_DIALOG_TAG = "info_dialog_tag"
    }

}
