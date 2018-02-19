@file:JvmName("MaybeExtensions")

package pl.droidsonroids.toast.app.utils.extensions.rx

import io.reactivex.Maybe
import pl.droidsonroids.toast.utils.Constants
import java.util.concurrent.TimeUnit

fun <T> Maybe<T>.addLoadingDelay(lastLoadingStartTimeMillis: Long, elapsedRealtime: Long): Maybe<T> = flatMap {
    Maybe.just(it)
            .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - elapsedRealtime, TimeUnit.MILLISECONDS)
}