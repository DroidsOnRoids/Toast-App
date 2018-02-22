package pl.droidsonroids.toast.utils

fun <T> MutableList<T>.removeFirst() {
    if (isNotEmpty()) {
        removeAt(0)
    }
}