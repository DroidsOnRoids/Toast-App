package pl.droidsonroids.toast.data.dto.speaker

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.toast.data.dto.ImageDto

@SuppressLint("ParcelCreator")
@Parcelize
data class SpeakerDto(
        val id: Long,
        val name: String,
        val job: String,
        val avatar: ImageDto
) : Parcelable