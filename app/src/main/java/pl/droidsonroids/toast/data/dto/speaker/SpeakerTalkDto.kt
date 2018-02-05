package pl.droidsonroids.toast.data.dto.speaker

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.toast.data.dto.event.EventDto

@SuppressLint("ParcelCreator")
@Parcelize
data class SpeakerTalkDto(
        val id: Long,
        val title: String,
        val description: String,
        val event: EventDto
) : Parcelable