package com.upmgeoinfo.culturamad.services.room

import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent

class CulturalEventRepository(
    private val culturalEventDao: CulturalEventDao
) {
    suspend fun getCulturalEvents(): List<CulturalEvent>{
        val entities = culturalEventDao.getCulturalEvents()
        return entities.map{
            CulturalEvent(
                id = it.id,
                title = it.title,
                category = it.category,
                description = it.description,
                latitude = it.latitude,
                longitude = it.longitude,
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

    suspend fun getCulturalEventsWithLocation(): List<CulturalEvent>{
        val entities = culturalEventDao.getCulturalEventsWithLocation()
        val toReturn = entities.map{
            CulturalEvent(
                id = it.id,
                title = it.title,
                category = it.category,
                description = it.description,
                latitude = it.latitude,
                longitude = it.longitude,
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
        return toReturn
    }

    suspend fun insertCulturalEvent(culturalEvent: CulturalEvent){
        val entity = CulturalEventEntity(
            id = culturalEvent.id!!,
            category = culturalEvent.category,
            title = culturalEvent.title,
            description = culturalEvent.description,
            latitude = culturalEvent.latitude,
            longitude = culturalEvent.longitude,
            address = culturalEvent.address,
            district = culturalEvent.district,
            neighborhood = culturalEvent.neighborhood,
            days = culturalEvent.days,
            frequency = culturalEvent.frequency,
            interval = culturalEvent.interval!!,
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
        culturalEventDao.insertCulturalEvent(entity)
    }

    suspend fun updateCulturalEvent(culturalEvent: CulturalEvent, bookmark: Boolean){
        val entity = CulturalEventEntity(
            id = culturalEvent.id!!,
            category = culturalEvent.category,
            title = culturalEvent.title,
            description = culturalEvent.description,
            latitude = culturalEvent.latitude,
            longitude = culturalEvent.longitude,
            address = culturalEvent.address,
            district = culturalEvent.district,
            neighborhood = culturalEvent.neighborhood,
            days = culturalEvent.days,
            frequency = culturalEvent.frequency,
            interval = culturalEvent.interval!!,
            dateStart = culturalEvent.dateStart,
            dateEnd = culturalEvent.dateEnd,
            hours = culturalEvent.hours,
            excludedDays = culturalEvent.excludedDays,
            place = culturalEvent.place,
            host = culturalEvent.host,
            price = culturalEvent.price,
            link = culturalEvent.link,
            bookmark = bookmark,
            review = culturalEvent.review!!
        )
        culturalEventDao.updateCulturalEvent(entity)
    }

    suspend fun updateCulturalEvent(culturalEvent: CulturalEvent, bookmark: Boolean, review: Int){
        val entity = CulturalEventEntity(
            id = culturalEvent.id!!,
            category = culturalEvent.category,
            title = culturalEvent.title,
            description = culturalEvent.description,
            latitude = culturalEvent.latitude,
            longitude = culturalEvent.longitude,
            address = culturalEvent.address,
            district = culturalEvent.district,
            neighborhood = culturalEvent.neighborhood,
            days = culturalEvent.days,
            frequency = culturalEvent.frequency,
            interval = culturalEvent.interval!!,
            dateStart = culturalEvent.dateStart,
            dateEnd = culturalEvent.dateEnd,
            hours = culturalEvent.hours,
            excludedDays = culturalEvent.excludedDays,
            place = culturalEvent.place,
            host = culturalEvent.host,
            price = culturalEvent.price,
            link = culturalEvent.link,
            bookmark = bookmark,
            review = review
        )
        culturalEventDao.updateCulturalEvent(entity)
    }

    suspend fun getCulturalEventEntityById(id: Int): CulturalEvent {
        val entity = culturalEventDao.getCulturalEventEntityById(id)
        val resultado =  CulturalEvent(
            id = entity.id,
            category = entity.category,
            title = entity.title,
            description = entity.description,
            latitude = entity.latitude,
            longitude = entity.longitude,
            address = entity.address,
            district = entity.district,
            neighborhood = entity.neighborhood,
            days = entity.days,
            frequency = entity.frequency,
            interval = entity.interval,
            dateStart = entity.dateStart,
            dateEnd = entity.dateEnd,
            hours = entity.hours,
            excludedDays = entity.excludedDays,
            place = entity.excludedDays,
            host = entity.host,
            price = entity.price,
            link = entity.link,
            bookmark = entity.bookmark,
            review = entity.review
        )
        return resultado
    }

    suspend fun deleteCulturalEvent(culturalEvent: CulturalEvent){
        val entity = CulturalEventEntity(
            id = culturalEvent.id!!,
            category = culturalEvent.category,
            title = culturalEvent.title,
            description = culturalEvent.description,
            latitude = culturalEvent.latitude,
            longitude = culturalEvent.longitude,
            address = culturalEvent.address,
            district = culturalEvent.district,
            neighborhood = culturalEvent.neighborhood,
            days = culturalEvent.days,
            frequency = culturalEvent.frequency,
            interval = culturalEvent.interval!!,
            dateStart = culturalEvent.dateStart,
            dateEnd = culturalEvent.dateEnd,
            hours = culturalEvent.hours,
            excludedDays = culturalEvent.excludedDays,
            place = culturalEvent.place,
            host = culturalEvent.host,
            price = culturalEvent.price,
            link = culturalEvent.link,
            bookmark = culturalEvent.bookmark,
            review = culturalEvent.review!!
        )
        culturalEventDao.deleteCulturalEvent(entity)
    }
}