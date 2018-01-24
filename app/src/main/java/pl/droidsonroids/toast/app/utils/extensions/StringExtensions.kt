@file:JvmName("StringExtensions")

package pl.droidsonroids.toast.app.utils.extensions

fun String.firstWord(): String {
    return this.substringBefore(" ")
}
