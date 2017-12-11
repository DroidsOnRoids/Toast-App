@file:JvmName("FragmentManagerExtensions")

package pl.droidsonroids.toast.app.utils

import android.annotation.SuppressLint
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

@SuppressLint("CommitTransaction")
fun FragmentManager.beginTransaction(transaction: FragmentTransaction.() -> Unit) {
    with(beginTransaction()) {
        transaction()
        commit()
    }
}