package com.upmgeoinfo.culturamad.viewmodels.main.model

import com.upmgeoinfo.culturamad.viewmodels.firestoredb.model.EventReview

data class MainState (
    val currentItem: String = "",
    val items: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList(),
    val isSplashScreenOnRender: Boolean = true,
    val searchValue: String = "",
    val activeSearchCategories: MutableList<String> = emptyList<String>().toMutableList(),
    val reviews: MutableList<EventReview> = emptyList<EventReview>().toMutableList()
)

//private fun emptyCulturalEvent(): CulturalEvent{
//    return CulturalEvent(
//        id = 0,
//        category = "",
//        title = "",
//        description = "",
//        latitude = "",
//        longitude = "",
//        address = "",
//        district = "",
//        neighborhood = "",
//        days = "",
//        frequency = "",
//        interval = 0,
//        dateStart = "",
//        dateEnd = "",
//        hours = "",
//        excludedDays = "",
//        place = "",
//        host = "",
//        price = "",
//        link = "",
//        bookmark = false,
//        review = 0
//    )
//}