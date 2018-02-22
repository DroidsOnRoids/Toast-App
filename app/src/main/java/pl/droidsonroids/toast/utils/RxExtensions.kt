package pl.droidsonroids.toast.utils

import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.Single
import pl.droidsonroids.toast.data.Page

fun <T : Any> Observable<T>.toPage(pageNo: Int, pageCount: Int): Single<Page<T>> {
    return toList()
            .map { Page(it, pageNo, pageCount) }
}

fun <T> ObservableField<T>.toObservable(): Observable<T> {
    return Observable.create { emitter ->
        addOnPropertyChangedCallback {
            emitter.onNext(it)
        }
    }
}