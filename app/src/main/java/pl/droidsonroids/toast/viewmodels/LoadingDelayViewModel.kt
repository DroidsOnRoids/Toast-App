package pl.droidsonroids.toast.viewmodels

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.viewmodels.speaker.Clock
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LoadingDelayViewModel @Inject constructor(private val clock: Clock) : DelayViewModel {

    private var lastLoadingStartTimeMillis = clock.elapsedRealtime()

    override fun updateLastLoadingStartTime() {
        lastLoadingStartTimeMillis = clock.elapsedRealtime()
    }

    override fun addLoadingDelay(completable: Completable) = completable.andThen {
        Completable.timer(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
    }


    override fun <T> addLoadingDelay(single: Single<T>) = single.flatMap {
        Single.just(it)
                .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
    }

    override fun <T> addLoadingDelay(maybe: Maybe<T>) = maybe.flatMap {
        Maybe.just(it)
                .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
    }

}