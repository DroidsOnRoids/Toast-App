package pl.droidsonroids.toast.utils

import java.util.*

val Date.isYesterdayOrEarlier
    get() = before(
            Calendar.getInstance().run {
                clear(Calendar.HOUR_OF_DAY)
                clear(Calendar.MINUTE)
                clear(Calendar.SECOND)
                clear(Calendar.MILLISECOND)
                time
            }
    )