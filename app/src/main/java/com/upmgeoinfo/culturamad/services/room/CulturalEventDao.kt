package com.upmgeoinfo.culturamad.services.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CulturalEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCulturalEvent(culturalEvent: CulturalEventEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateCulturalEvent(culturalEvent: CulturalEventEntity)

    @Query("SELECT * FROM CulturalEventEntity")
    suspend fun getCulturalEvents(): List<CulturalEventEntity>

    @Query("SELECT * FROM CulturalEventEntity WHERE latitude <> '' OR longitude <> ''")
    suspend fun getCulturalEventsWithLocation(): List<CulturalEventEntity>

    @Query("SELECT * FROM CulturalEventEntity WHERE id = :id")
    suspend fun getCulturalEventEntityById(id: Int): CulturalEventEntity

    @Query("DELETE FROM CulturalEventEntity WHERE id = :id")
    suspend fun deleteCulturalEvent(id: Int)

}