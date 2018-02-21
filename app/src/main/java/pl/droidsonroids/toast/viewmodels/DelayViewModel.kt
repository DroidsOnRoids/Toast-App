package pl.droidsonroids.toast.viewmodels

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single


interface DelayViewModel {

    fun <T> addLoadingDelay(single: Single<T>): Single<T>

    fun <T> addLoadingDelay(maybe: Maybe<T>): Maybe<T>

    fun addLoadingDelay(completable: Completable): Completable

    fun updateLastLoadingStartTime()
}