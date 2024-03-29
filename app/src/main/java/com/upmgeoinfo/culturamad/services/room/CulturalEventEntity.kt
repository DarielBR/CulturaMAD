package com.upmgeoinfo.culturamad.services.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class as model to dbLo single table
 */
@Entity
data class CulturalEventEntity(
    @PrimaryKey
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
//    val favorite: Boolean,
//    val review: String,
//    val rate: Float
)
