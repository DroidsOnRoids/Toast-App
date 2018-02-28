@file:JvmName("CharSequenceExtensions")

package pl.droidsonroids.toast.app.utils.extensions


val CharSequence.unicodeLength
    get() = toString().codePointCount(0, length)