package com.upmgeoinfo.culturamad.services.room

import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent

class CulturalEventRepository(
    private val culturalEventDao: CulturalEventDao
) {
    /**
     * transforms the information contained in dbLo to a list of CulturalEvents
     */
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
//                favorite = it.favorite,
//                rate = it.rate,
//                review = it.review
            )
        }
    }

    /**
     * transforms the information contained in dbLo to a list of CulturalEvents,
     * but only those with location.
     */
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
//                favorite = it.favorite,
//                rate = it.rate,
//                review = it.review
            )
        }
        return toReturn
    }

    /**
     * inserts a new cultural event into the dbLo
     */
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
            interval = culturalEvent.interval ?: 0,
            dateStart = culturalEvent.dateStart,
            dateEnd = culturalEvent.dateEnd,
            hours = culturalEvent.hours,
            excludedDays = culturalEvent.excludedDays,
            place = culturalEvent.place,
            host = culturalEvent.host,
            price = culturalEvent.price,
            link = culturalEvent.link,
//            favorite = culturalEvent.favorite,
//            review = culturalEvent.review,
//            rate = culturalEvent.rate ?: 0.0f
        )
        culturalEventDao.insertCulturalEvent(entity)
    }

   /* *//**
     * updates the information related to user interaction of a cultural event stored at dbLo
     * override: only the favorite state
     *//*
    suspend fun updateCulturalEvent(
        culturalEvent: CulturalEvent,
        favorite: Boolean
    ){
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
            favorite = favorite,
            rate = culturalEvent.rate ?: 0.0f,
            review = culturalEvent.review
        )
        culturalEventDao.updateCulturalEvent(entity)
    }*/

    /**
     * updates the information related to user interaction of a cultural event stored at dbLo
     */
    suspend fun updateCulturalEvent(
        culturalEvent: CulturalEvent,
//        favorite: Boolean,
//        review: String,
//        rate: Float
    ){
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
//            favorite = favorite,
//            review = review,
//            rate = rate
        )
        culturalEventDao.updateCulturalEvent(entity)
    }

    /**
     * search an item at the dbLo by its id and return it transformed into a CulturalEvent
     */
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
//            favorite = entity.favorite,
//            rate = entity.rate,
//            review = entity.review
        )
        return resultado
    }

    /**
     * deletes a cultural event from the dbLo
     */
    suspend fun deleteCulturalEvent(eventID: Int){
        culturalEventDao.deleteCulturalEvent(id = eventID)
    }
}