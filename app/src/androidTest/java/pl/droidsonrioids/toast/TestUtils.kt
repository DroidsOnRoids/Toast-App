package pl.droidsonrioids.toast

import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry

object TestUtils {
    fun getString(@StringRes stringRes: Int) = InstrumentationRegistry.getTargetContext().getString(stringRes)

}
