package pl.droidsonroids.toast.utils

import android.content.Context
import javax.inject.Inject


class StringResourceProvider @Inject constructor(private val context: Context){

    fun getString(res: Int): String =
            context.getString(res)
}