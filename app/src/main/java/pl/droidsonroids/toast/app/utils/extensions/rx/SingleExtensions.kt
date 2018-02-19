@file:JvmName("SingleExtensions")

package pl.droidsonroids.toast.app.utils.extensions.rx

import io.reactivex.Single
import pl.droidsonroids.toast.utils.Constants
import java.util.concurrent.TimeUnit

fun <T> Single<T>.addLoadingDelay(lastLoadingStartTimeMillis: Long, elapsedRealtime: Long): Single<T> = flatMap {
    Single.just(it)
            .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - elapsedRealtime, TimeUnit.MILLISECONDS)
}
