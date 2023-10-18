package com.upmgeoinfo.culturamad.services.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CulturalEventEntity::class], version = 1, exportSchema = false)
abstract class CulturalEventDatabase: RoomDatabase() {
    abstract val dao: CulturalEventDao
}