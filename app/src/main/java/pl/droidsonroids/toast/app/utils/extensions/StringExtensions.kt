@file:JvmName("StringExtensions")

package pl.droidsonroids.toast.app.utils.extensions

fun String.firstWord(): String {
    return this.substringBefore(" ")
}

fun String.cutToHundredChars(): String {
    return if (this.length > 100) {
        substring(0, 100)
    } else {
        this
    }
}
