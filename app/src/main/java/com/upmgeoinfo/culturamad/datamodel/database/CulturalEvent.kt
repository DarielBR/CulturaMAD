package com.upmgeoinfo.culturamad.datamodel.database

import android.media.MediaRouter.RouteCategory
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CulturalEvent(
    @PrimaryKey
    val id: Int,
    val category: String,
    val title: String,
    val description: String,

)
