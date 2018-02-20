@file:JvmName("RxExtensions")

package pl.droidsonroids.toast.app.utils.extensions

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonroids.toast.utils.Constants
import java.util.concurrent.TimeUnit

fun Completable.addLoadingDelay(lastLoadingStartTimeMillis: Long, elapsedRealtime: Long): Completable =
        delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - elapsedRealtime, TimeUnit.MILLISECONDS)

fun <T> Single<T>.addLoadingDelay(lastLoadingStartTimeMillis: Long, elapsedRealtime: Long): Single<T> = flatMap {
    Single.just(it)
            .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - elapsedRealtime, TimeUnit.MILLISECONDS)
}

fun <T> Maybe<T>.addLoadingDelay(lastLoadingStartTimeMillis: Long, elapsedRealtime: Long): Maybe<T> = flatMap {
    Maybe.just(it)
            .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - elapsedRealtime, TimeUnit.MILLISECONDS)
}
