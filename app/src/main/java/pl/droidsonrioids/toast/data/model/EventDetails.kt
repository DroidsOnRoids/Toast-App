package pl.droidsonrioids.toast.data.model

import java.util.*

interface EventDetails {
    val id: Int
    val title: String
    val date: Date
    val facebookId: String
    val placeName: String
    val placeStreet: String
    val placeCoordinates: Coordinates
    val coverImages: List<Image>
    val photos: List<Image>
}