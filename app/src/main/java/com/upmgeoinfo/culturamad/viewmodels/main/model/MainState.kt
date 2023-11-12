package com.upmgeoinfo.culturamad.viewmodels.main.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.upmgeoinfo.culturamad.viewmodels.firestoredb.model.EventReview

data class MainState (
    val currentItem: String = "",
    val items: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList(),
    val isSplashScreenOnRender: Boolean = true,
    val searchValue: String = "",
    val activeSearchCategories: MutableList<String> = emptyList<String>().toMutableList(),
    val reviews: MutableList<EventReview> = emptyList<EventReview>().toMutableList(),
    var deviceLocation: LatLng? = null,
    var isLocationPermissionGranted: Boolean = false,//should be in false
    var currentLazyColumType: LAZY_COLUMN_TYPE = LAZY_COLUMN_TYPE.ALL_EVENTS
)

enum class LAZY_COLUMN_TYPE{
    NEAR_BY,
    RATE,
    ALL_EVENTS,
    FAVORITES
}

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