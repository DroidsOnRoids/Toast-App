package pl.droidsonroids.toast

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.toast.app.home.MainActivity

class TestBottomMenu {
    @Suppress("unused")
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

}