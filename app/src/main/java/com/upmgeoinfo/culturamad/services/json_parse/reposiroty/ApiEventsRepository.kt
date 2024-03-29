package com.upmgeoinfo.culturamad.services.json_parse.reposiroty

import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.services.json_parse.`interface`.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiEventsRepository(){
    suspend fun parseJasonFile(
        onSuccess: (Boolean) -> Unit
    ): List<CulturalEvent> = withContext(Dispatchers.IO){
        val culturalEventList = emptyList<CulturalEvent>().toMutableList()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://datos.madrid.es/egob/catalogo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val response = service.getJsonFile()

        withContext(Dispatchers.Main){
            if (response.isSuccessful){
                response.body()?.graph?.forEach {
                    culturalEventList.add(
                        CulturalEvent(
                            id = it.id ?: 0,
                            category = it.type ?: "",
                            title = it.title ?: "",
                            description = it.description ?: "",
                            latitude = it.location?.latitude?.toString() ?: "",
                            longitude = it.location?.longitude?.toString() ?: "",
                            address = it.address?.area?.streetAddress ?: "",
                            district = it.address?.district?.id
                                ?.substring(it.address!!.district!!.id!!.lastIndexOf('/')+1) ?: "",
                            neighborhood = it.address?.area?.id
                                ?.substring(it.address!!.area!!.id!!.lastIndexOf('/')+1) ?: "",
                            days = it.recurrence?.days ?: "",
                            frequency = it.recurrence?.frequency ?: "",
                            interval = it.recurrence?.interval?.toInt() ?: 0,
                            dateStart = it.dtstart ?: "",
                            dateEnd = it.dtend ?: "",
                            hours = it.time ?: "",
                            excludedDays = it.excludedDays ?: "",
                            place = it.eventLocation ?: "",
                            host = it.organization?.organizationName ?: "",
                            price = it.price ?: "",
                            link = it.link ?: "",
                            favorite = false,
                            rate = 0.0,
                            review = ""
                        )
                    )
                }
                onSuccess.invoke(true)
            }else{
                onSuccess.invoke(false)
                //Log.e("Parsing", response.code().toString())
                //throw Exception("Error while parsing Json file -> " + response.code().toString())
            }
        }
        return@withContext culturalEventList.toList()
    }

    /**
     * returns a List of CulturalEvents obtained from and EndPoint, but only those with
     * Geographical information.
     */
    suspend fun getEventsWithLocation(
        onSuccess: (Boolean) -> Unit
    ): List<CulturalEvent> = withContext(Dispatchers.IO){
        val culturalEventList = emptyList<CulturalEvent>().toMutableList()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://datos.madrid.es/egob/catalogo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val response = service.getJsonFile()

        withContext(Dispatchers.Main){
            if (response.isSuccessful){
                response.body()?.graph?.forEach {
                    if (it.location != null){
                        culturalEventList.add(
                            CulturalEvent(
                                id = it.id ?: 0,
                                category = it.type ?: "",
                                title = it.title ?: "",
                                description = it.description ?: "",
                                latitude = it.location?.latitude?.toString() ?: "",
                                longitude = it.location?.longitude?.toString() ?: "",
                                address = it.address?.area?.streetAddress ?: "",
                                district = it.address?.district?.id
                                    ?.substring(it.address!!.district!!.id!!.lastIndexOf('/') + 1)
                                    ?: "",
                                neighborhood = it.address?.area?.id
                                    ?.substring(it.address!!.area!!.id!!.lastIndexOf('/') + 1)
                                    ?: "",
                                days = it.recurrence?.days ?: "",
                                frequency = it.recurrence?.frequency ?: "",
                                interval = it.recurrence?.interval?.toInt() ?: 0,
                                dateStart = it.dtstart ?: "",
                                dateEnd = it.dtend ?: "",
                                hours = it.time ?: "",
                                excludedDays = it.excludedDays ?: "",
                                place = it.eventLocation ?: "",
                                host = it.organization?.organizationName ?: "",
                                price = it.price ?: "",
                                link = it.link ?: "",
                                favorite = false,
                                rate = 0.0,
                                review = ""
                            )
                        )
                    }
                }
                onSuccess.invoke(true)
            }else{
                onSuccess.invoke(false)
                //Log.e("Parsing", response.code().toString())
                //throw Exception("Error while parsing Json file -> " + response.code().toString())
            }
        }
        return@withContext culturalEventList.toList()
    }
}