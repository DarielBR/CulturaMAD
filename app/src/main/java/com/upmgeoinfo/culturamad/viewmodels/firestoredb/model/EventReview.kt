package com.upmgeoinfo.culturamad.viewmodels.firestoredb.model

data class EventReview(
    var eventID: String = "",
    var userID: String = "",
    var review: String = "",
    var rate: Double = 0.0,
    var favorite: Boolean = false
)
