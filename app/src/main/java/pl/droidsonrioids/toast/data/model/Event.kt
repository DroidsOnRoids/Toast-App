package pl.droidsonrioids.toast.data.model

import java.util.*

interface Event {
    val id: Long
    val title: String
    val date: Date
    val coverImages: List<Image>
}