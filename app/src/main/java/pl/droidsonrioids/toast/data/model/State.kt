package pl.droidsonrioids.toast.data.model

sealed class State<out T> {
    data class Item<out T>(val item: T) : State<T>()
    class Error(val action: () -> Unit) : State<Nothing>()
    object Loading : State<Nothing>()
}

fun <T : Any> wrapWithState(item: T) = State.Item(item)