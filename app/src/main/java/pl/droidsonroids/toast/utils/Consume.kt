package pl.droidsonroids.toast.utils

inline fun consume(func: () -> Unit): Boolean {
   return true.also { func() }
}