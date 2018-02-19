@file:JvmName("CompletableExtensions")

package pl.droidsonroids.toast.app.utils.extensions.rx

import io.reactivex.Completable
import io.reactivex.Maybe
import pl.droidsonroids.toast.utils.Constants
import java.util.concurrent.TimeUnit

fun Completable.addLoadingDelay(lastLoadingStartTimeMillis: Long, elapsedRealtime: Long): Completable =
        delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - elapsedRealtime, TimeUnit.MILLISECONDS)
