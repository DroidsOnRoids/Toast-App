@file:JvmName("CharSequenceExtensions")

package pl.droidsonroids.toast.app.utils.extensions


fun CharSequence.getUnicodeLength()
        = this.toString().codePointCount(0, this.length)

