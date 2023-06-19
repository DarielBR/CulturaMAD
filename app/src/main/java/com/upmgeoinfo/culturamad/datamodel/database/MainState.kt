package com.upmgeoinfo.culturamad.datamodel.database

import com.upmgeoinfo.culturamad.datamodel.CulturalEvent

data class MainState (
    val currentItem: String = "",
    val items: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()
)

private fun emptyCulturalEvent(): CulturalEvent{
    return CulturalEvent(
        id = 0,
        category = "",
        title = "",
        description = "",
        latitude = "",
        longitude = "",
        address = "",
        district = "",
        neighborhood = "",
        days = "",
        frequency = "",
        interval = 0,
        dateStart = "",
        dateEnd = "",
        hours = "",
        excludedDays = "",
        place = "",
        host = "",
        price = "",
        link = "",
        bookmark = false,
        review = 0
    )
}