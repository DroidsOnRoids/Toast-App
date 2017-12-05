package pl.droidsonrioids.toast.data

data class Page<out T>(val items: List<T>, val pageNo: Int, val pageCount: Int)