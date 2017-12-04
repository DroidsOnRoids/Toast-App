package pl.droidsonrioids.toast.utils

import io.reactivex.Observable
import io.reactivex.Single
import pl.droidsonrioids.toast.data.model.Page

fun <T : Any> Observable<T>.toPage(pageNo: Int, pageCount: Int): Single<Page<T>> {
    return toList()
            .map { Page(it, pageNo, pageCount) }
}