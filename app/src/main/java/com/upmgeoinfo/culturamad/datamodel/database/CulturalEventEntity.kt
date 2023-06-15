package com.upmgeoinfo.culturamad.datamodel.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.sql.Date
import java.sql.Time

@Entity
data class CulturalEventEntity(
    @PrimaryKey
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val location: LatLng,
    val address: String,
    val district: String,
    val neighborhood: String,
    val days: String,
    val frequency: String,
    val interval: Int,
    val dateStart: Date,
    val dateEnd: Date,
    val hours: Time,
    val excludedDays: String,
    val place: String,
    val host: String,
    val price: Double,
    val link: String,
    val bookmark: Boolean,
    val review: Int
)
