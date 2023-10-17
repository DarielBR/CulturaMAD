package com.upmgeoinfo.culturamad.viewmodels.firestoredb.model

data class EventReview(
    val eventID: String = "",
    val userID: String = "",
    val review: String = "",
    val rate: Float = 0.0f,
    val bookmark: Boolean = false
)
