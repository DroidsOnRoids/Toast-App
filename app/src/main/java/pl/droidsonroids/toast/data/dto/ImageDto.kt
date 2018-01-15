package pl.droidsonroids.toast.data.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ImageDto(
        val originalSizeUrl: String,
        val thumbSizeUrl: String
) : Parcelable