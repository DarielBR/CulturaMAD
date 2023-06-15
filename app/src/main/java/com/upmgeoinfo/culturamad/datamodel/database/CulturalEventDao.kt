package com.upmgeoinfo.culturamad.datamodel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CulturalEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCulturalEvent(culturalEvent: CulturalEventEntity)

    @Query("SELECT * FROM CulturalEventEntity")
    suspend fun getCulturalEvents(): List<CulturalEventEntity>

}