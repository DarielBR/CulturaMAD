package com.upmgeoinfo.culturamad.datamodel

import com.google.android.gms.maps.model.LatLng
import java.sql.Date
import java.sql.Time

/**
 * This dataclass has bean created to bring data persistent to the app and to work with the Room
 * database, it's functionality might be overlapped with [CulturalEventMadrid] or [CulturalEventMadridItem]
 */
data class CulturalEvent(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val address: String,
    val district: String,
    val neighborhood: String,
    val days: String,
    val frequency: String,
    val interval: Int,
    val dateStart: String,
    val dateEnd: String,
    val hours: String,
    val excludedDays: String,
    val place: String,
    val host: String,
    val price: String,
    val link: String,
    var bookmark: Boolean,
    var review: Int
)
