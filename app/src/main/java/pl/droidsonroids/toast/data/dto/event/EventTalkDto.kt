package pl.droidsonroids.toast.data.dto.event

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.toast.data.dto.speaker.SpeakerDto

@SuppressLint("ParcelCreator")
@Parcelize
data class EventTalkDto(
        val id: Long,
        val title: String,
        val description: String,
        val speaker: SpeakerDto
) : Parcelable