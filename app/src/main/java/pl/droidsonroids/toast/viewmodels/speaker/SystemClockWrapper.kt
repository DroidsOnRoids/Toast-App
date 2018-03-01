package pl.droidsonroids.toast.viewmodels.speaker

import android.os.SystemClock
import javax.inject.Inject

interface Clock {
    fun elapsedRealtimeMillis(): Long
}

class SystemClockWrapper @Inject constructor() : Clock {
    override fun elapsedRealtimeMillis() = SystemClock.elapsedRealtime()
}
