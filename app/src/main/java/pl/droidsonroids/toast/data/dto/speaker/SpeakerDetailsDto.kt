package pl.droidsonroids.toast.data.dto.speaker

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.toast.data.dto.ImageDto


@SuppressLint("ParcelCreator")
@Parcelize
data class SpeakerDetailsDto(
        val id: Long,
        val name: String,
        val job: String,
        val bio: String,
        val avatar: ImageDto,
        val github: String?,
        val email: String?,
        val website: String?,
        val twitter: String?,
        val talks: List<SpeakerTalkDto>
) : Parcelable