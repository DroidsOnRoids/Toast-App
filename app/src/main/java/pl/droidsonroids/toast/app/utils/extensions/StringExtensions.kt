@file:JvmName("StringExtensions")

package pl.droidsonroids.toast.app.utils.extensions

fun String.firstWord(): String {
    return if (this.contains(" ")) {
        this.substring(0, this.indexOf(" "))
    } else {
        ""
    }

}
