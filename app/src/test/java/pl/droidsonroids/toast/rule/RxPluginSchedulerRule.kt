package pl.droidsonroids.toast.rule

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.ExternalResource

open class RxPluginSchedulerRule(private val computationScheduler: Scheduler = Schedulers.trampoline()) : ExternalResource() {

    override fun before() {
        RxJavaPlugins.setComputationSchedulerHandler { computationScheduler }
        RxJavaPlugins.setIoSchedulerHandler { computationScheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { computationScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { computationScheduler }
    }

    override fun after() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}