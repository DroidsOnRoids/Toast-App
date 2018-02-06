package pl.droidsonroids.toast.utils

import java.util.*

val Date.isYesterdayOrEarlier
    get() = before(
            Calendar.getInstance().run {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                time
            }
    )