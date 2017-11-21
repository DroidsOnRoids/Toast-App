package pl.droidsonrioids.toast.app.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu

import kotlinx.android.synthetic.main.activity_main.*
import pl.droidsonrioids.toast.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupViewPager()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupViewPager() {
        homeViewPager.adapter = HomePagerAdapter(supportFragmentManager)
        with(homeTabLayout) {
            setupWithViewPager(homeViewPager)
            getTabAt(0)?.setIcon(R.drawable.ic_events)
            getTabAt(1)?.setIcon(R.drawable.ic_lecturers)
            getTabAt(2)?.setIcon(R.drawable.ic_contact)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}

