package pl.droidsonrioids.toast.app.home

import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.app.base.BaseActivity

class MainActivity : BaseActivity() {

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
}

