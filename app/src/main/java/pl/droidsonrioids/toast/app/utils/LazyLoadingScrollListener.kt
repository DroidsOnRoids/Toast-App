package pl.droidsonrioids.toast.app.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class LazyLoadingScrollListener(private val loadNextPage: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        (recyclerView.layoutManager as? LinearLayoutManager)
                ?.let {
                    loadIfRecyclerEnd(it)
                }
    }

    private fun loadIfRecyclerEnd(it: LinearLayoutManager) {
        if (isRecyclerEnd(it)) {
            loadNextPage()
        }
    }

    private fun isRecyclerEnd(it: LinearLayoutManager) =
            it.itemCount - 1 == it.findLastVisibleItemPosition()
}