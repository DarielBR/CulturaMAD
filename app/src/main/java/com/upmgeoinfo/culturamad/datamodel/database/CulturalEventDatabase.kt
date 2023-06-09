package com.upmgeoinfo.culturamad.datamodel.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CulturalEventEntity::class], version = 1)
abstract class CulturalEventDatabase: RoomDatabase() {
    abstract val dao: CulturalEventDao
}