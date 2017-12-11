@file:JvmName("FragmentManagerExtensions")

package pl.droidsonroids.toast.app.utils

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import pl.droidsonroids.toast.R


fun FragmentTransaction.attachChosenFragment(supportFragmentManager: FragmentManager,
                                             fragmentTag: String,
                                             fragmentCreator: () -> Fragment) {
    val fragmentToReplace = supportFragmentManager.findFragmentByTag(fragmentTag)
    fragmentToReplace?.let {
        attach(it)
    } ?: add(R.id.fragmentContainer, fragmentCreator(), fragmentTag)
}

fun FragmentTransaction.setFragmentsAnimation() {
    setCustomAnimations(R.anim.animation_translated_cross_fade_in, R.anim.animation_cross_fade_out)
}

fun FragmentTransaction.detachCurrentFragment(currentFragment: Fragment?) {
    currentFragment?.let { detach(it) }
}

@SuppressLint("CommitTransaction")
fun FragmentManager.beginTransaction(transaction: FragmentTransaction.() -> Unit) {
    with(beginTransaction()) {
        transaction()
        commit()
    }
}