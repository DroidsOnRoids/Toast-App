package pl.droidsonroids.toast.viewmodels

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.viewmodels.speaker.Clock
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LoadingDelayViewModel @Inject constructor(private val clock: Clock) : DelayViewModel {

    var lastLoadingStartTimeMillis = clock.elapsedRealtime()

    override fun addLoadingDelay(completable: Completable): Completable {
        this@LoadingDelayViewModel.lastLoadingStartTimeMillis = clock.elapsedRealtime()
        return completable.andThen {
            Completable.timer(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
        }
    }

    override fun <T> addLoadingDelay(single: Single<T>): Single<T> {
        this@LoadingDelayViewModel.lastLoadingStartTimeMillis = clock.elapsedRealtime()
        return single.flatMap {
            Single.just(it)
                    .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
        }
    }

    override fun <T> addLoadingDelay(maybe: Maybe<T>): Maybe<T> {
        this@LoadingDelayViewModel.lastLoadingStartTimeMillis = clock.elapsedRealtime()
        return maybe.flatMap {
            Maybe.just(it)
                    .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
        }
    }
}