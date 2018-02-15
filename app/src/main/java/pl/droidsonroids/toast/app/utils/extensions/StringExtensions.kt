@file:JvmName("StringExtensions")

package pl.droidsonroids.toast.app.utils.extensions

private const val MAX_LENGTH = 100
private const val START_INDEX = 0

fun String.firstWord(): String {
    return this.substringBefore(" ")
}

fun String.cutToHundredChars(): String {
    return if (this.length > MAX_LENGTH) {
        substring(START_INDEX, MAX_LENGTH)
    } else {
        this
    }
}
