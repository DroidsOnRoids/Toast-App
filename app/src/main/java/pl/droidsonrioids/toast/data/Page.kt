package pl.droidsonrioids.toast.data

data class Page<out T>(val items: List<T>, val pageNumber: Int, val allPagesCount: Int)