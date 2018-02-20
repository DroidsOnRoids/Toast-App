package pl.droidsonroids.toast.viewmodels

import io.reactivex.Completable
import io.reactivex.Single


interface DelayViewModel {
    fun Completable.addLoadingDelay(): Completable

    fun <T> Single<T>.addLoadingDelay(): Single<T>
}