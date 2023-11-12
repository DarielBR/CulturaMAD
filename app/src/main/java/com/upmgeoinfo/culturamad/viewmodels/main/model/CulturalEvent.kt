package com.upmgeoinfo.culturamad.viewmodels.main.model

/**
 * This dataclass has bean created to bring data persistent to the app and to work with the Room
 * database, it's functionality might be overlapped with CulturalEventMadrid or CulturalEventMadridItem
 */
data class CulturalEvent(
    val id: Int? = null,
    val category: String = "",
    val title: String = "",
    val description: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val address: String = "",
    val district: String = "",
    val neighborhood: String = "",
    val days: String = "",
    val frequency: String = "",
    val interval: Int?  = null,
    val dateStart: String = "",
    val dateEnd: String = "",
    val hours: String = "",
    val excludedDays: String = "",
    val place: String = "",
    val host: String = "",
    val price: String = "",
    val link: String = "",
    var favorite: Boolean = false,
    var rate: Double = 0.0,
    var averageRate: Double = 0.0,
    var review: String = ""
)
