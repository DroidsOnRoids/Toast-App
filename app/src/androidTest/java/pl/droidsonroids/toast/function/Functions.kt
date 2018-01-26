package pl.droidsonroids.toast.function

import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry

fun getString(@StringRes stringRes: Int) = InstrumentationRegistry.getTargetContext().getString(stringRes)

fun getStringWithoutFormattingArguments(@StringRes stringRes: Int) =
        InstrumentationRegistry.getTargetContext().getString(stringRes).replace(" %s", "")

