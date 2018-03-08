package pl.droidsonroids.toast.utils

import java.util.*

val Date.isYesterdayOrEarlier
    get() = before(todayMidnight().time)

val Date.isToday
    get() = this in todayMidnight().run {
        add(Calendar.DAY_OF_MONTH, 1)
        todayMidnight().time..time
    }

private fun todayMidnight(): Calendar {
    return Calendar.getInstance().apply {
        clear(Calendar.HOUR_OF_DAY)
        clear(Calendar.MINUTE)
        clear(Calendar.SECOND)
        clear(Calendar.MILLISECOND)
    }
}