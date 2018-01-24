package pl.droidsonroids.toast

import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins
import android.support.test.runner.AndroidJUnitRunner


class RxAndroidJUnitRunner : AndroidJUnitRunner() {
    override fun onStart() {
        RxJavaPlugins.setInitIoSchedulerHandler(
                Rx2Idler.create("RxJava 2.x IO Scheduler"))
        RxJavaPlugins.setInitComputationSchedulerHandler(
                Rx2Idler.create("RxJava 2.x Computation Scheduler"))
        RxJavaPlugins.setInitNewThreadSchedulerHandler(
                Rx2Idler.create("RxJava 2.x New Thread Scheduler"))
        super.onStart()
    }
}
