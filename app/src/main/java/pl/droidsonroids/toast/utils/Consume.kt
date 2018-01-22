package pl.droidsonroids.toast.utils

fun consume(func: () -> Unit): Boolean {
    func()
    return true
}