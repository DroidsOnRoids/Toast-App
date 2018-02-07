package pl.droidsonroids.toast.data.dto.event

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.droidsonroids.toast.data.dto.ImageDto
import java.util.*

@SuppressLint("ParcelCreator")
@Parcelize
data class EventDto(
        val id: Long,
        val title: String,
        val date: Date,
        val coverImages: List<ImageDto>
) : Parcelable