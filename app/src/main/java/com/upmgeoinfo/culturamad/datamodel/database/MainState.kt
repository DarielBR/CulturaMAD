package com.upmgeoinfo.culturamad.datamodel.database

import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.services.json_parse.api_model.ApiItem

data class MainState (
    val currentItem: String = "",
    val apiItems: List<ApiItem> = emptyList(),//List with all @Id's at @Graph
    val items: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList(),
    val isSplashScreenOnRender: Boolean = true,
    val searchValue: String = "",
    val activeSearchCategories: MutableList<String> = emptyList<String>().toMutableList()
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