@file:JvmName("FloatExtensions")

package pl.droidsonroids.toast.app.utils.extensions


fun Float.countPercent(percent: Float): Float {
    return percent * this / 100
}