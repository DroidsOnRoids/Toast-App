package pl.droidsonroids.toast.viewmodels

import io.reactivex.Completable
import io.reactivex.Single
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.viewmodels.speaker.Clock
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LoadingDelayViewModel @Inject constructor(private val clock: Clock) : DelayViewModel {

    var lastLoadingStartTimeMillis = clock.elapsedRealtime()

    override fun Completable.addLoadingDelay(): Completable {
        this@LoadingDelayViewModel.lastLoadingStartTimeMillis = clock.elapsedRealtime()
        return andThen {
            delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
        }
    }


    override fun <T> Single<T>.addLoadingDelay(): Single<T> {
        this@LoadingDelayViewModel.lastLoadingStartTimeMillis = clock.elapsedRealtime()
        return flatMap {
            Single.just(it)
                    .delay(Constants.MIN_LOADING_DELAY_MILLIS + lastLoadingStartTimeMillis - clock.elapsedRealtime(), TimeUnit.MILLISECONDS)
        }

    }
}