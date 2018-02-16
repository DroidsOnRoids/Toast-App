package pl.droidsonroids.toast.viewmodels.speaker

import android.os.SystemClock
import javax.inject.Inject

interface Clock {
    fun elapsedRealtime(): Long
}

class SystemClockWrapper @Inject constructor() : Clock {
    override fun elapsedRealtime() = SystemClock.elapsedRealtime()

}
