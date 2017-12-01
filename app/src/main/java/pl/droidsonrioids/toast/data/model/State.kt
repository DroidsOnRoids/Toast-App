package pl.droidsonrioids.toast.data.model

sealed class State<out T> {
    data class Item<out T>(val item: T) : State<T>()
    class Error(action: () -> Unit) : State<Nothing>()
    object Loading : State<Nothing>()
}