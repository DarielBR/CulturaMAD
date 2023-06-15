package com.upmgeoinfo.culturamad.datamodel.database

import com.upmgeoinfo.culturamad.datamodel.CulturalEvent

class CulturalEventRepository(
    private val culturalEventDao: CulturalEventDao
) {

    suspend fun getCulturalEvent(): List<CulturalEvent>{
        val entities = culturalEventDao.getCulturalEvents()
        return entities.map{
            CulturalEvent(
                id = it.id,
                title = it.title,
                category = it.category,
                description = it.description,
                location = it.location,
                address = it.address,
                district = it.district,
                neighborhood = it.neighborhood,
                days = it.days,
                frequency = it.frequency,
                interval = it.interval,
                dateStart = it.dateStart,
                dateEnd = it.dateEnd,
                hours = it.hours,
                excludedDays = it.excludedDays,
                place = it.place,
                host = it.host,
                price = it.price,
                link = it.link,
                bookmark = it.bookmark,
                review = it.review
            )
        }
    }

    suspend fun insertCulturalEvent(culturalEvent: CulturalEvent){
        val entity = CulturalEventEntity(
            id = culturalEvent.id,
            category = culturalEvent.category,
            title = culturalEvent.title,
            description = culturalEvent.description,
            location = culturalEvent.location,
            address = culturalEvent.address,
            district = culturalEvent.district,
            neighborhood = culturalEvent.neighborhood,
            days = culturalEvent.days,
            frequency = culturalEvent.frequency,
            interval = culturalEvent.interval,
            dateStart = culturalEvent.dateStart,
            dateEnd = culturalEvent.dateEnd,
            hours = culturalEvent.hours,
            excludedDays = culturalEvent.excludedDays,
            place = culturalEvent.place,
            host = culturalEvent.host,
            price = culturalEvent.price,
            link = culturalEvent.link,
            bookmark = false,
            review = 0
        )
    }
}