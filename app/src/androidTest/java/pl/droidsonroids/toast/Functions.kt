package pl.droidsonroids.toast

import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry

fun getString(@StringRes stringRes: Int) = InstrumentationRegistry.getTargetContext().getString(stringRes)

